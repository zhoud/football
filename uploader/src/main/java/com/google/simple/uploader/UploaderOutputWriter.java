package com.google.simple.uploader;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.OutputWriter;

import java.io.IOException;
import java.util.ArrayList;

public class UploaderOutputWriter extends OutputWriter<ArrayList<Entity>> {
  // Datastore connection.
  private DatastoreService datastore;

  @Override
  public void beginShard() throws IOException {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void write(ArrayList<Entity> entities) throws IOException {
    for (Entity entity : entities) {
      datastore.put(entity);
    }
  }
}
