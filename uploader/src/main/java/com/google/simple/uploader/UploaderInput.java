package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.Input;
import com.google.appengine.tools.mapreduce.InputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploaderInput extends Input<UploaderGameKey> {
  // The season whose info is being processed by this MapReduce job.
  private String season;

  public UploaderInput(String season) {
    this.season = season;
  }

  /**
   * Only one reader is needed, as the week and team lists are each fetched by a single GET.
   */
  @Override
  public List<UploaderInputReader> createReaders() throws IOException {
    List<UploaderInputReader> readers = new ArrayList<>();
    readers.add(new UploaderInputReader(season));
    return readers;
  }
}
