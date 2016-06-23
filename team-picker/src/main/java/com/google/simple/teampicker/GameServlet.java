package com.google.simple.teampicker;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    String team = request.getParameter("team_id");
    String weekString = request.getParameter("week");
    String otherTeam = request.getParameter("other_team_id");
    String locationString = request.getParameter("location");
    String teamScoreString = request.getParameter("team_score");
    String otherScoreString = request.getParameter("other_score");

    if (team == null ||
        weekString == null ||
        otherTeam == null ||
        locationString == null ||
        teamScoreString == null ||
        otherScoreString == null) {
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.GAME_MISSING_VALUES));
      return;
    }

    if (team.equals(otherTeam)) {
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.GAME_SAME_TEAM_TWICE));
      return;
    }

    Game.Location location = null;
    Game.Location otherLocation = null;
    if (locationString.equals("home_game")) {
      location = Game.Location.HOME;
      otherLocation = Game.Location.AWAY;
    } else if (locationString.equals("away_game")) {
      location = Game.Location.AWAY;
      otherLocation = Game.Location.HOME;
    } else if (locationString.equals("neutral_site")) {
      location = Game.Location.NEUTRAL;
      otherLocation = Game.Location.NEUTRAL;
    }

    int week = 0;
    int teamScore = 0;
    int otherScore = 0;
    try {
      week = Integer.parseInt(weekString);
      teamScore = Integer.parseInt(teamScoreString);
      otherScore = Integer.parseInt(otherScoreString);
    } catch (NumberFormatException e) {
      // weekString will never trigger the exception.
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.GAME_NON_NEGATIVE_INTEGER_SCORE));
      return;
    }

    if (teamScore < 0 || otherScore < 0) {
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.GAME_NON_NEGATIVE_INTEGER_SCORE));
      return;
    }

    // Check that there isn't already a game during that particular week. This is a constraint due
    // to games needing to remain symmetrical.
    if (doesGameExist(season, team, week) || doesGameExist(season, otherTeam, week)) {
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.GAME_ALREADY_EXISTS));
      return;
    }

    // Store the game entity. A game entry is added for both teams.
    Key<Season> seasonKey = Key.create(Season.class, season);
    Game mainGameEntity =
        new Game(seasonKey, team, (long) week, otherTeam, location, teamScore, otherScore);
    ObjectifyService.ofy().save().entity(mainGameEntity).now();

    Game otherGameEntity =
        new Game(seasonKey, otherTeam, (long) week, team, otherLocation, otherScore, teamScore);
    ObjectifyService.ofy().save().entity(otherGameEntity).now();

    response.sendRedirect(ServletMessages.getRedirectUrl(season, ServletMessages.GAME_SUCCESS));
  }

  private boolean doesGameExist(String season, String team, int week) {
    Key<Team> teamKey = Key.create(Key.create(Season.class, season), Team.class, team);
    List<Game> possibleGames = ObjectifyService.ofy()
        .load()
        .type(Game.class)
        .ancestor(teamKey)
        .filterKey(Key.create(teamKey, Game.class, (long) week))
        .list();
    return !possibleGames.isEmpty();
  }
}
