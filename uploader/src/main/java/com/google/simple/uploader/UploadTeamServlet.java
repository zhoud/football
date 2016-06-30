package com.google.simple.uploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadTeamServlet extends HttpServlet {
  // Utility object that handles various upload tasks.
  private UploadUtil uploader = new UploadUtil();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    if (!uploader.checkSeason(season)) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    String team = request.getParameter("team");
    String fullTeamName = request.getParameter("team_name");
    if (team == null || fullTeamName == null || !GameFetcher.checkTeam(team)) {
      response.sendRedirect("uploader.jsp?error=true");
      return;
    }

    List<GameFetcher.Team> teams = new ArrayList<>();
    teams.add(new GameFetcher.Team(team, fullTeamName));

    List<Integer> weeks = GameFetcher.fetchWeeks(season);
    uploader.uploadGames(season, teams, weeks);

    response.sendRedirect("uploader.jsp?success=true");
  }
}
