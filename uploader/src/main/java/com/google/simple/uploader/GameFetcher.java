package com.google.simple.uploader;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameFetcher {
  // The fetch service used to query URLs.
  private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();

  // Stored apiKey. It will be read from a hidden config file the first time it is needed.
  private static String apiKey = null;

  // JSON deserializer.
  private static Gson gson = new Gson();

  /**
   * Returns a list of teams. Up to 100 teams are returned.
   */
  public static List<Team> fetchTeams() {
    return fetchTeams(100);
  }

  /**
   * Returns a list of teams. Up to limit teams are returned.
   */
  public static List<Team> fetchTeams(int limit) {
    List<Team> teams = new ArrayList<>();
    String content = getURLResponseContents("teams/", limit);
    if (content == null) {
      return teams;
    }

    GsonTeamList teamList = gson.fromJson(content, GsonTeamList.class);
    for (GsonTeam team : teamList.teams()) {
      teams.add(new Team(team.team(), team.fullTeamName()));
    }
    return teams;
  }

  /**
   * Returns a list of weeks in the given season.
   */
  public static List<Integer> fetchWeeks(String season) {
    List<Integer> weeks = new ArrayList<>();
    String content = getURLResponseContents("season/" + season + "/week/", 100);
    if (content == null) {
      return weeks;
    }

    GsonWeekList weekList = gson.fromJson(content, GsonWeekList.class);
    for (GsonWeek week : weekList.weeks()) {
      weeks.add(week.week());
    }
    return weeks;
  }

  /**
   * Returns the game played by the given team in the given week.
   */
  public static Game fetchGame(String season, String team, int week) {
    String content = getURLResponseContents("season/" + season + "/week/" + week + "/" + team, 1);
    if (content == null) {
      return null;
    }

    GsonGame game = gson.fromJson(content, GsonGame.class);
    if (game == null || game.teams() == null || game.results() == null) {
      return null;
    }
    return new Game(
        week,
        game.teams().homeTeam(),
        game.teams().awayTeam(),
        game.results().homeScore(),
        game.results().awayScore());
  }

  private static String getURLResponseContents(String apiCall, int limit) {
    HTTPRequest request = null;
    String urlString = "https://collegefootballapi.com/api/1.0/" + apiCall + "?limit=" + limit;
    try {
      request = new HTTPRequest(new URL(urlString));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    request.addHeader(new HTTPHeader("Authorization", "apikey " + getApiKey()));

    HTTPResponse response = null;
    try {
      response = fetcher.fetch(request);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return new String(response.getContent());
  }

  private static String getApiKey() {
    if (apiKey != null) {
      return apiKey;
    }

    Properties properties = new Properties();
    InputStream input = null;
    try {
      input = GameFetcher.class.getResourceAsStream("/hidden.properties");
      properties.load(input);
      apiKey = properties.getProperty("api_key");
    } catch (IOException e) {
      e.printStackTrace();
      apiKey = "badkey";
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return apiKey;
  }

  /**
   * Class used for JSON deserialization of a list of teams.
   */
  private static class GsonTeamList {
    // List of teams.
    private GsonTeam[] teams;

    public GsonTeamList() {}

    public GsonTeam[] teams() {
      return teams;
    }
  }

  /**
   * Class used for JSON deserialization of a team.
   */
  private static class GsonTeam {
    // Team ID.
    private String team;

    // Full team name.
    private String full_name;

    public GsonTeam() {}

    public String team() {
      return team;
    }

    public String fullTeamName() {
      return full_name;
    }
  }

  /**
   * Class used for JSON deserialization of a list of weeks in a season.
   */
  private static class GsonWeekList {
    // List of weeks.
    private GsonWeek[] weeks;

    public GsonWeekList() {}

    public GsonWeek[] weeks() {
      return weeks;
    }
  }

  /**
   * Class used for JSON deserialization of a single week in a season.
   */
  private static class GsonWeek {
    // Week number.
    private int week;

    public GsonWeek() {}

    public int week() {
      return week;
    }
  }

  /**
   * Class used for JSON deserialization of a single game during a single week in a season.
   */
  private static class GsonGame {
    // The two teams that played in the game.
    private GsonGameTeams teams;

    // The results of the game.
    private GsonGameResults results;

    public GsonGame() {}

    public GsonGameTeams teams() {
      return teams;
    }

    public GsonGameResults results() {
      return results;
    }
  }

  /**
   * Class used for JSON deserialization of the two teams participating in a game.
   */
  private static class GsonGameTeams {
    // Home team ID.
    private String home_team;

    // Away team ID.
    private String away_team;

    public GsonGameTeams() {}

    public String homeTeam() {
      return home_team;
    }

    public String awayTeam() {
      return away_team;
    }
  }

  /**
   * Class used for JSON deserialization of the results of a game.
   */
  private static class GsonGameResults {
    // Home team's score.
    private int home_score;

    // Away team's score.
    private int away_score;

    public GsonGameResults() {}

    public int homeScore() {
      return home_score;
    }

    public int awayScore() {
      return away_score;
    }
  }

  /**
   * Team information packaged into a single class.
   */
  public static class Team {
    // Team ID.
    private String team;

    // Full team name.
    private String fullTeamName;

    public Team(String team, String fullTeamName) {
      this.team = team;
      this.fullTeamName = fullTeamName;
    }

    public String team() {
      return team;
    }

    public String fullTeamName() {
      return fullTeamName;
    }

    @Override
    public String toString() {
      return team + ": " + fullTeamName;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Team)) {
        return false;
      }

      Team other = (Team) obj;
      return team.equals(other.team)
          && fullTeamName.equals(other.fullTeamName);
    }
  }

  /**
   * Game information packed into a single class.
   */
  public static class Game implements Serializable {
    // Week of the season.
    private int week;

    // Home team ID.
    private String homeTeam;

    // Away team ID.
    private String awayTeam;

    // Home team score.
    private int homeScore;

    // Away team score.
    private int awayScore;

    public Game(int week, String homeTeam, String awayTeam, int homeScore, int awayScore) {
      this.week = week;
      this.homeTeam = homeTeam;
      this.awayTeam = awayTeam;
      this.homeScore = homeScore;
      this.awayScore = awayScore;
    }

    public int week() {
      return week;
    }

    public String homeTeam() {
      return homeTeam;
    }

    public String awayTeam() {
      return awayTeam;
    }

    public int homeScore() {
      return homeScore;
    }

    public int awayScore() {
      return awayScore;
    }

    @Override
    public String toString() {
      return "home:" + homeTeam + ";away:" + awayTeam + ";" + homeScore + "-" + awayScore;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Game)) {
        return false;
      }

      Game other = (Game) obj;
      return homeTeam.equals(other.homeTeam)
          && awayTeam.equals(other.awayTeam)
          && homeScore == other.homeScore
          && awayScore == other.awayScore;
    }
  }
}
