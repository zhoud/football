package com.google.simple.uploader;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.tools.mapreduce.InputReader;

import java.io.IOException;
import java.util.NoSuchElementException;

public class UploaderInputReader extends InputReader<String> {
  // The season whose data is being processed by this MapReduce job.
  private String season;

  public UploaderInputReader(String season) {
    this.season = season;
  }

  @Override
  public void beginShard() throws IOException {
    URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
  }

  @Override
  public String next() throws IOException, NoSuchElementException {
    return null;
  }
}
