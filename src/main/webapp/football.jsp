<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.google.simple.football.Game" %>
<%@ page import="com.google.simple.football.Season" %>
<%@ page import="com.google.simple.football.Team" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<%
  // Load the season and team data. Verify that the GET parameters for season and team are valid.
  String seasonString = request.getParameter("season");
  String teamString = request.getParameter("team");

  List<Season> seasons = ObjectifyService.ofy()
      .load()
      .type(Season.class)
      .orderKey(true)
      .list();

  boolean seasonFound = false;
  for (Season season : seasons) {
    if (season.season().equals(seasonString)) {
      seasonFound = true;
      break;
    }
  }

  List<Team> teams = new ArrayList<Team>();
  if (seasonFound) {
    teams = ObjectifyService.ofy()
        .load()
        .type(Team.class)
        .ancestor(Key.create(Season.class, seasonString))
        .order("fullTeamName")
        .list();

    boolean teamFound = false;
    for (Team team : teams) {
      if (team.team().equals(teamString)) {
        teamFound = true;
        break;
      }
    }
    if (!teamFound) {
      teamString = null;
    }
  } else {
    seasonString = null;
  }
%>

<body>
<form action="/football.jsp" method="GET">
  <div>
    Choose a season:
    <select name="season">
<%
  for (Season season : seasons) {
    pageContext.setAttribute("season", season.season());
    pageContext.setAttribute(
        "default_selected",
        season.season().equals(seasonString) ? "selected=\"selected\"" : "");
%>
      <option value="${fn:escapeXml(season)}" ${default_selected}>${fn:escapeXml(season)}</option>
<%
  }
%>
    </select>&nbsp;
<%
  if (seasonString != null) {
%>
    Choose a team:
    <select name="team">
<%
    for (Team team : teams) {
      pageContext.setAttribute("team", team.team());
      pageContext.setAttribute("team_name", team.fullTeamName());
      pageContext.setAttribute(
          "default_selected",
          team.team().equals(teamString) ? "selected=\"selected\"" : "");
%>
      <option value="${fn:escapeXml(team)}" ${default_selected}>${fn:escapeXml(team_name)}</option>
<%
    }
%>
    </select>
<%
  }
%>
    <br /><br />
    <input type="submit" value="Lookup season/team" />
  </div>
</form>
</body>
</html>
