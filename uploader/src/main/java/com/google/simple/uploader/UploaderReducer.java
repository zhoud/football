package com.google.simple.uploader;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.mapreduce.Reducer;

import java.util.ArrayList;

public class UploaderReducer extends Reducer<String, GameFetcher.Game, ArrayList<Entity>> {
  @Override
  public void reduce(String teamKey, ReducerInput<GameFetcher.Game> games) {
    String[] teamKeyParts = teamKey.split(";");
    if (teamKeyParts.length == 3) {
      String season = teamKeyParts[0];
      String team = teamKeyParts[1];
      String fullTeamName = teamKeyParts[2];

      ArrayList<Entity> entities = new ArrayList<>();
      Key seasonKey = KeyFactory.createKey("Season", season);

      Entity teamEntity = new Entity("Team", team, seasonKey);
      teamEntity.setIndexedProperty("fullTeamName", fullTeamName);
      entities.add(teamEntity);

      Key teamKey = KeyFactory.createKey(seasonKey, "Team", team);
      while (games.hasNext()) {
        GameFetcher.Game game = games.next();
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

        gameEntity.setUnindexedProperty("otherTeam", otherTeam);
        gameEntity.setUnindexedProperty("location", location);
        gameEntity.setUnindexedProperty("mainScore", mainScore);
        gameEntity.setUnindexedProperty("otherScore", otherScore);
        entities.add(gameEntity)
      }
    }
  }
}
