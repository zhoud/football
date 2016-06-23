package com.google.simple.teampicker;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Helper class that finds the shortest path between two teams based on wins.
 */
public class TeamGraphSolver {
  // Finds the shortest path between startTeam and endTeam where each link in the path between two
  // teams is present if the first team beat the second team at some point during the season.
  // Returns null if no path exists.
  public static TeamGraphPath shortestPath(String season, String startTeam, String endTeam) {
    // Track which teams have already been visited in the path search.
    Set<String> visited = new HashSet<>();
    visited.add(startTeam);

    // Queue maintaining partial paths being searched.
    Queue<TeamGraphPath> paths = new LinkedList<>();
    paths.add(new TeamGraphPath(startTeam));

    while (!paths.isEmpty()) {
      TeamGraphPath currentPath = paths.remove();
      List<Game> wins = findWins(season, currentPath.lastTeam(), visited);
      for (Game nextGame : wins) {
        TeamGraphPath nextPath = currentPath.clone();
        nextPath.addGame(nextGame);
        if (nextGame.otherTeam().equals(endTeam)) {
          return nextPath;
        }
        paths.add(nextPath);
      }
    }

    return null;
  }

  private static List<Game> findWins(String season, String team, Set<String> visited) {
    Key<Season> seasonKey = Key.create(Season.class, season);
    List<Game> games = ObjectifyService.ofy()
        .load()
        .type(Game.class)
        .ancestor(Key.create(seasonKey, Team.class, team))
        .orderKey(false)
        .list();

    List<Game> wins = new ArrayList<>();
    for (Game game : games) {
      if (game.won() && !visited.contains(game.otherTeam())) {
        visited.add(game.otherTeam());
        wins.add(game);
      }
    }

    return wins;
  }
}
