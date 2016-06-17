package com.google.simple.football;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class which searches a team's games for wins, and tracks wins already counted.
 */
public class GraphHelper {

  private final Set<String> visited;

  public GraphHelper() {
    visited = new HashSet<>();
  }

  // Given a list of teams, find the list of all teams that were beat by them at some point
  // during the current season. Only teams not yet found before will be returned. All teams visited
  // will be marked as such.
  public List<GraphLink> findWins(List<String> teams) {
    List<Game> allGames = new ArrayList<>();
    for (String team : teams) {
      List<Game> games = ObjectifyService.ofy()
          .load()
          .type(Game.class)
          .ancestor(Key.create(Team.class, team))
          .orderKey(false)
          .list();
      allGames.addAll(games);
    }

    List<GraphLink> graphLinks = new ArrayList<>();
    for (Game game : allGames) {
      if (game.won() && !visited.contains(game.otherTeam())) {
        visited.add(game.otherTeam());
        graphLinks.add(new GraphLink(game.mainTeam(), game.otherTeam()));
      }
    }

    return graphLinks;
  }
}
