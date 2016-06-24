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
   * Return a list of weeks in the given season.
   */
  public static List<Integer> fetchWeeks(String season) {
    HTTPRequest request = null;
    try {
      request =
          new HTTPRequest(
              new URL("https://collegefootballapi.com/api/1.0/season/" + season + "/week/"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return new ArrayList<Integer>();
    }
    request.addHeader(new HTTPHeader("Authorization", "apikey " + getApiKey()));

    HTTPResponse response = null;
    try {
      response = fetcher.fetch(request);
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<Integer>();
    }

    String content = new String(response.getContent());
    GsonWeekList weekList = gson.fromJson(content, GsonWeekList.class);
    List<Integer> weeksResult = new ArrayList<>();
    for (GsonWeek week : weekList.weeks()) {
      weeksResult.add(week.week());
    }
    return weeksResult;
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
}
