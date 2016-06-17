package com.google.simple.football;

/**
 * A single link in the football win graph. It stores the names of the two teams involved.
 */
public class GraphLink {

  private final String winningTeam;
  private final String losingTeam;

  public GraphLink(String winningTeam, String losingTeam) {
    this.winningTeam = winningTeam;
    this.losingTeam = losingTeam;
  }

  public String winningTeam() {
    return winningTeam;
  }

  public String losingTeam() {
    return losingTeam;
  }
}
