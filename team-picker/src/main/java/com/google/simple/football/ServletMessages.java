package com.google.simple.football;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServletMessages {

  public static final int SEASON_MISSING_VALUES = 0;
  public static final int SEASON_SUCCESS = 1;

  public static final int TEAM_MISSING_VALUES = 2;
  public static final int TEAM_SUCCESS = 3;

  public static final int GAME_MISSING_VALUES = 4;
  public static final int GAME_SAME_TEAM_TWICE = 5;
  public static final int GAME_NON_NEGATIVE_INTEGER_SCORE = 6;
  public static final int GAME_ALREADY_EXISTS = 7;
  public static final int GAME_SUCCESS = 8;

  private static final Map<Integer, String> messageMap;
  static {
    Map<Integer, String> initializerMap = new HashMap<>();
    initializerMap.put(
        SEASON_MISSING_VALUES, "Season must be provided.");
    initializerMap.put(
        SEASON_SUCCESS, "Season successfully saved!");
    initializerMap.put(
        TEAM_MISSING_VALUES, "Season, full team name, and team ID must be provided.");
    initializerMap.put(
        TEAM_SUCCESS, "Team successfully saved!");
    initializerMap.put(
        GAME_MISSING_VALUES, "All game values must be provided.");
    initializerMap.put(
        GAME_SAME_TEAM_TWICE, "Game must occur between two different teams.");
    initializerMap.put(
        GAME_NON_NEGATIVE_INTEGER_SCORE, "Scores must be non-negative integers.");
    initializerMap.put(
        GAME_ALREADY_EXISTS, "Game already exists.");
    initializerMap.put(
        GAME_SUCCESS, "Game successfully saved!");
    messageMap = Collections.unmodifiableMap(initializerMap);
  }

  public static String getMessage(int messageCode) {
    return messageMap.get(messageCode);
  }

  public static String getRedirectUrl(int messageCode) {
    String url = "/upload.jsp";
    if (messageMap.containsKey(messageCode)) {
      url += "?message=" + messageCode;
    }
    return url;
  }

  public static String getRedirectUrl(String season, int messageCode) {
    String url = getRedirectUrl(messageCode);
    url += messageMap.containsKey(messageCode) ? "&" : "?";
    url += "season=" + season;
    return url;
  }
}
