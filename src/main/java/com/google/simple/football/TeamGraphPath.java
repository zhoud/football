package com.google.simple.football;

import java.util.ArrayList;
import java.util.List;

/**
 * A path of teams connected by wins used by TeamGraphSolver.
 */
public class TeamGraphPath implements Cloneable {
  private List<String> teams;

  // Zero-arg constructor for clone().
  private TeamGraphPath() {}

  public TeamGraphPath(String initialTeam) {
    teams = new ArrayList<>();
    teams.add(initialTeam);
  }

  public List<String> teams() {
    return teams;
  }

  public String last() {
    return teams.get(teams.size() - 1);
  }

  public void addTeam(String team) {
    teams.add(team);
  }

  @Override
  public TeamGraphPath clone() {
    TeamGraphPath teamGraphPath = new TeamGraphPath();
    for (String team : teams) {
      teamGraphPath.addTeam(team);
    }
    return teamGraphPath;
  }
}
