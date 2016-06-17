package com.google.simple.football;

import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the user adding a new season via upload.jsp.
 */
public class SeasonServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    if (season == null) {
      response.sendRedirect(ServletMessages.getRedirectUrl(ServletMessages.SEASON_MISSING_VALUES));
      return;
    }
    season = season.trim();
    ObjectifyService.ofy().save().entity(new Season(season)).now();

    response.sendRedirect(ServletMessages.getRedirectUrl(season, ServletMessages.SEASON_SUCCESS));
  }
}
