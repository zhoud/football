package com.google.simple.uploader;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Input;
import com.google.appengine.tools.mapreduce.Mapper;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshaller;
import com.google.appengine.tools.mapreduce.Marshallers;
import com.google.appengine.tools.mapreduce.Output;
import com.google.appengine.tools.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadServlet extends HttpServlet {
  // Utility object that handles various upload tasks.
  private UploadUtil uploader = new UploadUtil();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    if (!uploader.checkSeason(season)) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    Input<UploaderGameKey> input = new UploaderInput(season);
    Mapper<UploaderGameKey, UploaderTeamKey, GameFetcher.Game> mapper = new UploaderMapper();
    Reducer<UploaderTeamKey, GameFetcher.Game, ArrayList<Entity>> reducer = new UploaderReducer();
    Output<ArrayList<Entity>, Object> output = new UploaderOutput();

    Marshaller<UploaderTeamKey> keyMarshaller = Marshallers.getSerializationMarshaller();
    Marshaller<GameFetcher.Game> valueMarshaller = Marshallers.getSerializationMarshaller();

    MapReduceSpecification<
        UploaderGameKey, UploaderTeamKey, GameFetcher.Game, ArrayList<Entity>, Object>
            specification =
                new MapReduceSpecification.Builder<>(input, mapper, reducer, output)
                    .setKeyMarshaller(keyMarshaller)
                    .setValueMarshaller(valueMarshaller)
                    .setJobName("UploaderMapReduce")
                    .setNumReducers(10)
                    .build();

    MapReduceSettings settings =
        new MapReduceSettings.Builder()
            .setBucketName("simple-test-project.google.com.a.appspot.com")
            .setWorkerQueueName("mapreduce-workers")
            .setModule("mapreduce")
            .build();

    String id = MapReduceJob.start(specification, settings);
    response.sendRedirect("/_ah/pipeline/status.html?root=" + id);
  }
}
