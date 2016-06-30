package com.google.simple.uploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadWeekServlet extends HttpServlet {
  // Utility object that handles various upload tasks.
  private UploadUtil uploader = new UploadUtil();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    if (!uploader.checkSeason(season)) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    boolean valid = true;
    List<Integer> validWeeks = GameFetcher.fetchWeeks(season);
    List<Integer> weeks = null;

    String weekString = request.getParameter("week");
    if (weekString != null) {
      try {
        int week = Integer.parseInt(weekString);
        if (validWeeks.contains(week)) {
          weeks = new ArrayList<>();
          weeks.add(week);
        } else {
          valid = false;
        }
      } catch (NumberFormatException e) {
        valid = false;
      }
    } else {
      valid = false;
    }

    if (!valid) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    List<GameFetcher.Team> teams = GameFetcher.fetchTeams();
    uploader.uploadGames(season, teams, weeks);

    response.sendRedirect("uploader.jsp?success=true");
  }
}
