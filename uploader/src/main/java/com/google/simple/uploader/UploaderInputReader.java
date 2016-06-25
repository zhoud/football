package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.InputReader;

import java.io.IOException;
import java.util.NoSuchElementException;

public class UploaderInputReader extends InputReader<String> {
  // The season whose data is being processed by this MapReduce job.
  private String season;

  // List of all teams that can be fetched.
  private List<GameFetcher.Team> teams;

  // List of all weeks in the season.
  private List<Integer> weeks;

  // Index into the team list during iteration.
  private int teamIndex = 0;

  // Index into the week list during iteration.
  private int weekIndex = 0;

  public UploaderInputReader(String season) {
    this.season = season;
  }

  @Override
  public void beginShard() throws IOException {
    teams = GameFetcher.fetchTeams();
    weeks = GameFetcher.fetchWeeks(season);
  }

  @Override
  public String next() throws IOException, NoSuchElementException {
    if (teamIndex >= teams.size() || weekIndex >= weeks.size()) {
      throw NoSuchElementException();
    }

    // Father forgive me, for I have sinned.
    GameFetcher team = teams.get(teamIndex);
    String theWorst = season + ";"
        + weeks.get(weekIndex) + ";"
        + team.team() + ";"
        + team.fullTeamName();

    weekIndex++;
    if (weekIndex >= weeks.size()) {
      teamIndex++;
      weekIndex = 0;
    }

    return theWorst;
  }
}
