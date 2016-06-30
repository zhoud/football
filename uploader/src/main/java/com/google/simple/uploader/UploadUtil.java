package com.google.simple.uploader;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.List;

public class UploadUtil {
  // Used to talk to Datastore.
  private DatastoreService datastore;

  // Used to rate limit our requests to the college football API.
  private Queue queue;

  public UploadUtil() {
    datastore = DatastoreServiceFactory.getDatastoreService();
    queue = QueueFactory.getDefaultQueue();
  }

  // Checks if the season is valid. If it is, the season entity is inserted into Datastore and the
  // function returns true. Otherwise, the function returns false.
  public boolean checkSeason(String season) {
    if (season == null || !GameFetcher.checkSeason(season)) {
      return false;
    }

    datastore.put(new Entity("Season", season));
    return true;
  }

  public void uploadGames(String season, List<GameFetcher.Team> teams, List<Integer> weeks) {
    Key seasonKey = KeyFactory.createKey("Season", season);
    for (GameFetcher.Team team : teams) {
      Entity teamEntity = new Entity("Team", team.team(), seasonKey);
      teamEntity.setProperty("fullTeamName", team.fullTeamName());
      datastore.put(teamEntity);

      // We put the work of fetching data from the college football API in a task queue using the
      // default rate limit of 5/s. This is to avoid hammering the API with a burst of requests at
      // once when an entire season is loaded.
      for (int week : weeks) {
        queue.add(
            TaskOptions.Builder
                .withUrl("/game_handler")
                .param("season", season)
                .param("team", team.team())
                .param("week", "" + week));
      }
    }
  }
}
