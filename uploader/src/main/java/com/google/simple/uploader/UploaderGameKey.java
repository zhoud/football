package com.google.simple.uploader;

import java.io.Serializable;

/**
 * Serializable key uniquely identifying a game. Used as input for UploaderMapper.
 */
public class UploaderGameKey implements Serializable {
  // The season.
  private String season;

  // The team ID.
  private String team;

  // The team's full name. While not part of the key, it is included so that the corresponding team
  // entity can later be added.
  private String fullTeamName;

  // The week of the game in question.
  private int week;

  public UploaderGameKey(String season, String team, String fullTeamName, int week) {
    this.season = season;
    this.team = team;
    this.fullTeamName = fullTeamName;
    this.week = week;
  }

  public String season() {
    return season;
  }

  public String team() {
    return team;
  }

  public String fullTeamName() {
    return fullTeamName;
  }

  public int week() {
    return week;
  }
}
