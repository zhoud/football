package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.Mapper;

public class UploaderMapper extends Mapper<String, String, GameFetcher.Game> {
  @Override
  public void map(String gameKey) {
    String[] gameKeyParts = gameKey.split(";");
    if (gameKeyParts.length == 4) {
      try {
        String season = gameKeyParts[0];
        int week = Integer.parseInt(gameKeyParts[1]);
        String team = gameKeyParts[2];
        String fullTeamName = gameKeyParts[3];

        GameFetcher.Game game = GameFetcher.fetchGame(season, team, week);
        if (game != null && team.equals(game.homeTeam()) || team.equals(game.awayTeam())) {
          String key = season + ";" + team + ";" + fullTeamName;
          emit(key, game);
        }
      } catch (NumberFormatException e) {
        // Ignore this game.
      }
  }
}
