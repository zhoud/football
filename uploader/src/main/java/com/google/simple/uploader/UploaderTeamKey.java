package com.google.simple.uploader;

import java.io.Serializable;

/**
 * Serializable key uniquely identifying a team. Used as a key for UploaderReducer.
 */
public class UploaderTeamKey implements Serializable {
  // The season.
  private String season;

  // The team ID.
  private String team;

  // The team's full name.
  private String fullTeamName;

  public UploaderTeamKey(String season, String team, String fullTeamName) {
    this.season = season;
    this.team = team;
    this.fullTeamName = fullTeamName;
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
}
