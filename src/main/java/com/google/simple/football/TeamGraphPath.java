package com.google.simple.football;

import java.util.ArrayList;
import java.util.List;

/**
 * A path of teams connected by wins used by TeamGraphSolver. The path is implicitly described by a
 * series of games. The following constraint must hold: games[i].otherTeam == games[i + 1].mainTeam.
 */
public class TeamGraphPath implements Cloneable {
  // The list of games representing the path of teams with wins over each other.
  private List<Game> games;

  // The initial team is stored for convenience and simplicity of code, as it allows clients to use
  // lastTeam even when no games have been added.
  private String initialTeam;

  public TeamGraphPath(String initialTeam) {
    games = new ArrayList<>();
    this.initialTeam = initialTeam;
  }

  public List<Game> games() {
    return games;
  }

  public String lastTeam() {
    if (games.isEmpty()) {
      return initialTeam;
    }
    return games.get(games.size() - 1).otherTeam();
  }

  public void addGame(Game game) {
    games.add(game);
  }

  @Override
  public TeamGraphPath clone() {
    TeamGraphPath teamGraphPath = new TeamGraphPath(initialTeam);
    for (Game game : games) {
      teamGraphPath.games.add(game);
    }
    return teamGraphPath;
  }
}
