package com.google.simple.uploader;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Output;
import com.google.appengine.tools.mapreduce.OutputWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UploaderOutput extends Output<ArrayList<Entity>, Object> {
  @Override
  public List<UploaderOutputWriter> createWriters(int numShards) {
    List<UploaderOutputWriter> writers = new ArrayList<>();
    writers.add(new UploaderOutputWriter());
    return writers;
  }

  @Override
  public Object finish(
      Collection<? extends OutputWriter<ArrayList<Entity>>> writers) throws IOException {
    return null;
  }
}
