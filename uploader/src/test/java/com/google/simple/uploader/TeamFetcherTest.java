package com.google.simple.uploader;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

import java.util.List;

public class TeamFetcherTest {
  @Test
  public void testTeamFetch() {
    List<String> teamList = TeamFetcher.fetchTeams();
    assertThat(teamList).hasSize(128);
    assertThat(teamList)
        .containsExactly(
            "stanford",
            "oregon",
            "california",
            "arizona");
  }
}
