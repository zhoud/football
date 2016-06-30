package com.google.simple.uploader;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

public class UploadServlet extends HttpServlet {
  // Utility object that handles various upload tasks.
  private UploadUtil uploader = new UploadUtil();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    if (!uploader.checkSeason(season)) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    List<GameFetcher.Team> teams = GameFetcher.fetchTeams();
    List<Integer> weeks = GameFetcher.fetchWeeks(season);
    uploader.uploadGames(season, teams, weeks);

    response.sendRedirect("uploader.jsp?success=true");
  }
}
