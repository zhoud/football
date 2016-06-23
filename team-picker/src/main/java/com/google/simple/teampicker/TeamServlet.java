package com.google.simple.teampicker;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TeamServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    String fullName = request.getParameter("full_name");
    String team = request.getParameter("team_id");

    if (season == null) {
      response.sendRedirect(ServletMessages.getRedirectUrl(ServletMessages.TEAM_MISSING_VALUES));
      return;
    }
    if (fullName == null || team == null) {
      response.sendRedirect(
          ServletMessages.getRedirectUrl(season, ServletMessages.TEAM_MISSING_VALUES));
      return;
    }

    Team teamEntity = new Team(season, team, fullName);
    ObjectifyService.ofy().save().entity(teamEntity).now();

    response.sendRedirect(ServletMessages.getRedirectUrl(season, ServletMessages.TEAM_SUCCESS));
  }
}
