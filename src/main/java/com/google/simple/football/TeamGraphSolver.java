package com.google.simple.football;

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
    Set<String> visited = new HashSet<>();
    visited.add(startTeam);
    Queue<TeamGraphPath> paths = new LinkedList<>();
    paths.add(new TeamGraphPath(startTeam));

    while (!paths.isEmpty()) {
      TeamGraphPath currentPath = paths.remove();
      List<String> wins = findWins(season, currentPath.last(), visited);
      for (String nextTeam : wins) {
        TeamGraphPath nextPath = currentPath.clone();
        nextPath.addTeam(nextTeam);
        if (nextTeam.equals(endTeam)) {
          return nextPath;
        }
        paths.add(nextPath);
      }
    }

    return null;
  }

  private static List<String> findWins(String season, String team, Set<String> visited) {
    Key<Season> seasonKey = Key.create(Season.class, season);
    List<Game> games = ObjectifyService.ofy()
        .load()
        .type(Game.class)
        .ancestor(Key.create(seasonKey, Team.class, team))
        .orderKey(false)
        .list();

    List<String> nextTeams = new ArrayList<>();
    for (Game game : games) {
      String otherTeam = game.otherTeam();
      if (game.won() && !visited.contains(otherTeam)) {
        visited.add(otherTeam);
        nextTeams.add(otherTeam);
      }
    }

    return nextTeams;
  }
}
