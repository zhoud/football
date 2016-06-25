package com.google.simple.uploader;

import com.google.appengine.tools.mapreduce.InputReader;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class UploaderInputReader extends InputReader<UploaderGameKey> {
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
  public UploaderGameKey next() throws IOException, NoSuchElementException {
    if (teamIndex >= teams.size() || weekIndex >= weeks.size()) {
      throw new NoSuchElementException();
    }

    GameFetcher.Team team = teams.get(teamIndex);
    UploaderGameKey gameKey =
        new UploaderGameKey(season, team.team(), team.fullTeamName(), weeks.get(weekIndex));

    weekIndex++;
    if (weekIndex >= weeks.size()) {
      teamIndex++;
      weekIndex = 0;
    }

    return gameKey;
  }
}
