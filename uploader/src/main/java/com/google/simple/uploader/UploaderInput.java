package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.Input;
import com.google.appengine.tools.mapreduce.InputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploaderInput extends Input<String> {
  // The season whose info is being processed by this MapReduce job.
  private String season;

  public UploaderInput(String season) {
    this.season = season;
  }

  /**
   * Only one reader is needed, since the list of teams is fetched by a single url GET call.
   */
  @Override
  public List<UploaderInputReader> createReaders() throws IOException {
    ArrayList<UploaderInputReader> readers = new ArrayList<>();
    readers.add(new UploaderInputReader(season));
    return readers;
  }
}
