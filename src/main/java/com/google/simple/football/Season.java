package com.google.simple.football;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Entity for a season. Seasons don't store any data, but serve as an ancestor key.
 */
@Entity
public class Season {
  @Id
  private final String season;

  public Season(String season) {
    this.season = season;
  }

  public String season() {
    return season;
  }
}
