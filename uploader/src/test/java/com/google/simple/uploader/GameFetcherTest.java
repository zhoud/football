package com.google.simple.uploader;

import static com.google.common.truth.Truth.assertThat;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GameFetcherTest {
  // Local testing config.
  private LocalServiceTestHelper helper;

  @Before
  public void setUp() {
    helper = new LocalServiceTestHelper(new LocalURLFetchServiceTestConfig());
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testCheckSeason() {
    assertThat(GameFetcher.checkSeason("2015")).isTrue();
    assertThat(GameFetcher.checkSeason("2010")).isTrue();
    assertThat(GameFetcher.checkSeason("2018")).isFalse();
    assertThat(GameFetcher.checkSeason("blah")).isFalse();
  }

  @Test
  public void testCheckTeam() {
    assertThat(GameFetcher.checkTeam("stanford")).isTrue();
    assertThat(GameFetcher.checkTeam("garbanzo")).isFalse();
  }

  @Test
  public void testFetchTeams() {
    List<GameFetcher.Team> teams = GameFetcher.fetchTeams(5);
    assertThat(teams).hasSize(5);
    assertThat(teams)
        .containsExactly(
            new GameFetcher.Team("akron", "University of Akron"),
            new GameFetcher.Team("air_force", "Air Force"),
            new GameFetcher.Team("arizona", "University of Arizona"),
            new GameFetcher.Team("alabama", "University of Alabama"),
            new GameFetcher.Team("appalachian_st", "Appalachian State University"));
  }

  @Test
  public void testFetchWeeks() {
    List<Integer> weeks = GameFetcher.fetchWeeks("2015");
    assertThat(weeks).hasSize(11);
    assertThat(weeks).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
  }

  @Test
  public void testFetchGame() {
    GameFetcher.Game game = GameFetcher.fetchGame("2015", "stanford", 1);
    assertThat(game).isEqualTo(new GameFetcher.Game(1, "northwestern", "stanford", 16, 6));
  }
}
