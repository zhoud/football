package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.Mapper;

public class UploaderMapper extends Mapper<UploaderGameKey, UploaderTeamKey, GameFetcher.Game> {
  @Override
  public void map(UploaderGameKey gameKey) {
    GameFetcher.Game game = GameFetcher.fetchGame(gameKey.season(), gameKey.team(), gameKey.week());
    String team = gameKey.team();
    if (game != null && (team.equals(game.homeTeam()) || team.equals(game.awayTeam()))) {
      emit(new UploaderTeamKey(gameKey.season(), gameKey.team(), gameKey.fullTeamName()), game);
    }
  }
}
