package com.google.simple.football;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * The information for a single game between two teams, from the perspective of one of those teams.
 * Each of the two teams involved will have a Game version, but from different perspectives. The
 * data is duplicated in order to reduce the number of queries that need to be made to Datastore.
 */
@Entity
public class Game {
  // Location is relative to the main team.
  public enum Location {
    HOME,
    AWAY,
    NEUTRAL,
  }

  // The team whose perspective is described by this Game.
  @Parent
  private Key<Team> mainTeam;

  // Which week during the season the game occurred. Since at most one game is played each week,
  // the week number serves as a unique identifier.
  @Id
  private Long week;

  // Those jerks.
  private final String otherTeam;

  // The location of the game for the main team.
  private final Location location;

  // Points scored by the main team.
  private final int mainScore;

  // Points scored by the other team.
  private final int otherScore;

  public Game(
      Key<Season> seasonKey,
      String mainTeam,
      Long week,
      String otherTeam,
      Location location,
      int mainScore,
      int otherScore) {
    this.mainTeam = Key.create(seasonKey, Team.class, mainTeam);
    this.week = week;
    this.otherTeam = otherTeam;
    this.location = location;
    this.mainScore = mainScore;
    this.otherScore = otherScore;
  }

  public String mainTeam() {
    return mainTeam.getName();
  }

  public Long week() {
    return week;
  }

  public String otherTeam() {
    return otherTeam;
  }

  public Location location() {
    return location;
  }

  public int mainScore() {
    return mainScore;
  }

  public int otherScore() {
    return otherScore;
  }

  public boolean won() {
    return mainScore > otherScore;
  }
}
