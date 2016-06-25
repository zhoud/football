package com.google.simple.uploader;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.mapreduce.Reducer;
import com.google.appengine.tools.mapreduce.ReducerInput;

import java.util.ArrayList;

public class UploaderReducer extends Reducer<UploaderTeamKey, GameFetcher.Game, ArrayList<Entity>> {
  @Override
  public void reduce(UploaderTeamKey uploaderTeamKey, ReducerInput<GameFetcher.Game> games) {
    String season = uploaderTeamKey.season();
    String team = uploaderTeamKey.team();
    String fullTeamName = uploaderTeamKey.fullTeamName();

    ArrayList<Entity> entities = new ArrayList<>();
    Key seasonKey = KeyFactory.createKey("Season", season);

    Entity teamEntity = new Entity("Team", team, seasonKey);
    teamEntity.setProperty("fullTeamName", fullTeamName);
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

      gameEntity.setProperty("otherTeam", otherTeam);
      gameEntity.setProperty("location", location);
      gameEntity.setProperty("mainScore", mainScore);
      gameEntity.setProperty("otherScore", otherScore);
      entities.add(gameEntity);
    }
  }
}
