package com.google.simple.teampicker;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.List;

/**
 * Entity for a team's general data.
 */
@Entity
public class Team {
  @Parent
  Key<Season> season;

  // The key for a team is a unique string identifier.
  @Id
  private String team;

  // The full team name used for display.
  @Index
  private String fullTeamName;

  // No-arg constructor for Datastore.
  private Team() {}

  public Team(String season, String team, String fullTeamName) {
    this.season = Key.create(Season.class, season);
    this.team = team;
    this.fullTeamName = fullTeamName;
  }

  public String season() {
    return season.getName();
  }

  public String team() {
    return team;
  }

  public String fullTeamName() {
    return fullTeamName;
  }
}
