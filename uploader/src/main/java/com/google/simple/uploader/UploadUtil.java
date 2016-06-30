package com.google.simple.uploader;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadUtil {
  // Used to talk to Datastore.
  private DatastoreService datastore;

  public UploadUtil() {
    datastore = DatastoreServiceFactory.getDatastoreService();
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
    Map<String, String> fullTeamNameMap = new HashMap<>();
    Map<String, List<GameFetcher.Game>> gameMap = new HashMap<>();

    for (GameFetcher.Team team : teams) {
      for (int week : weeks) {
        fullTeamNameMap.put(team.team(), team.fullTeamName());
        GameFetcher.Game game = GameFetcher.fetchGame(season, team.team(), week);
        if (game != null
            && (team.team().equals(game.homeTeam()) || team.team().equals(game.awayTeam()))) {
          if (!gameMap.containsKey(team.team())) {
            gameMap.put(team.team(), new ArrayList<GameFetcher.Game>());
          }
          gameMap.get(team.team()).add(game);
        }
      }
    }

    Key seasonKey = KeyFactory.createKey("Season", season);
    for (Map.Entry<String, List<GameFetcher.Game>> entry : gameMap.entrySet()) {
      String team = entry.getKey();
      List<GameFetcher.Game> games = entry.getValue();

      Entity teamEntity = new Entity("Team", team, seasonKey);
      teamEntity.setProperty("fullTeamName", fullTeamNameMap.get(team));
      datastore.put(teamEntity);

      Key teamKey = KeyFactory.createKey(seasonKey, "Team", team);
      for (GameFetcher.Game game : games) {
        Entity gameEntity = new Entity("Game", (long) game.week(), teamKey);

        String otherTeam;
        String location;
        int mainScore;
        int otherScore;
        if (team.equals(game.homeTeam())) {
          otherTeam = game.awayTeam();
          location = "HOME";
          mainScore = game.homeScore();
          otherScore = game.awayScore();
        } else {
          otherTeam = game.homeTeam();
          location = "AWAY";
          mainScore = game.awayScore();
          otherScore = game.homeScore();
        }

        gameEntity.setProperty("otherTeam", otherTeam);
        gameEntity.setProperty("location", location);
        gameEntity.setProperty("mainScore", mainScore);
        gameEntity.setProperty("otherScore", otherScore);

        datastore.put(gameEntity);
      }
    }
  }
}
