package com.google.simple.uploader;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameHandler extends HttpServlet {
  // Logs queue task status.
  private static final Logger log = Logger.getLogger(GameHandler.class.getName());

  // Writes to the datastore.
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    String team = request.getParameter("team");
    int week;
    try {
      week = Integer.parseInt(request.getParameter("week"));
    } catch (NumberFormatException e) {
      log.severe("Non-integer week value passed to GameHandler: " + request.getParameter("week"));
      return;
    }

    log.info("Worker is processing (" + season + ", " + team + ", " + week + ").");

    Key seasonKey = KeyFactory.createKey("Season", season);
    Key teamKey = KeyFactory.createKey(seasonKey, "Team", team);
    GameFetcher.Game game = GameFetcher.fetchGame(season, team, week);

    String otherTeam;
    String location;
    int mainScore;
    int otherScore;
    if (team.equals(game.homeTeam())) {
      otherTeam = game.awayTeam();
      location = "HOME";
      mainScore = game.homeScore();
      otherScore = game.awayScore();
    } else if (team.equals(game.awayTeam())) {
      otherTeam = game.homeTeam();
      location = "AWAY";
      mainScore = game.awayScore();
      otherScore = game.homeScore();
    } else {
      log.warning(
          "Game retrieved where team is not home or away: ("
              + season + ", "
              + team + ", "
              + week + ") vs ("
              + game.homeTeam() + ", "
              + game.awayTeam() + ").");
      return;
    }

    Entity gameEntity = new Entity("Game", (long) week, teamKey);
    gameEntity.setProperty("otherTeam", otherTeam);
    gameEntity.setProperty("location", location);
    gameEntity.setProperty("mainScore", mainScore);
    gameEntity.setProperty("otherScore", otherScore);
    datastore.put(gameEntity);
  }
}
