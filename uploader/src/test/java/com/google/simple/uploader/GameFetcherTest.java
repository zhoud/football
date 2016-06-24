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
  public void testFetchWeeks() {
    List<Integer> weekList = GameFetcher.fetchWeeks("2015");
    assertThat(weekList).hasSize(10);
    assertThat(weekList).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }
}
