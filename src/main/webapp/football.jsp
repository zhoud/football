<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.google.simple.football.Game" %>
<%@ page import="com.google.simple.football.Season" %>
<%@ page import="com.google.simple.football.Team" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

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
  Map<String, Team> teamMap = new HashMap<String, Team>();
  if (seasonFound) {
    teams = ObjectifyService.ofy()
        .load()
        .type(Team.class)
        .ancestor(Key.create(Season.class, seasonString))
        .order("fullTeamName")
        .list();

    for (Team team : teams) {
      teamMap.put(team.team(), team);
    }
    if (!teamMap.containsKey(teamString)) {
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

<%
  if (seasonString != null && teamString != null) {
    pageContext.setAttribute("season", seasonString);
    pageContext.setAttribute("team", teamString);
    pageContext.setAttribute("team_name", teamMap.get(teamString).fullTeamName());

    List<Game> games = ObjectifyService.ofy()
        .load()
        .type(Game.class)
        .ancestor(Key.create(Key.create(Season.class, seasonString), Team.class, teamString))
        .orderKey(false)
        .list();
%>
<h2>${fn:escapeXml(team_name)} ${fn:escapeXml(season)} Season</h2>
<table>
  <tr>
    <th>Week</th>
    <th>Opponent</th>
    <th>Location</th>
    <th>Result</th>
  </tr>
<%
    for (Game game : games) {
      pageContext.setAttribute("week", game.week());
      pageContext.setAttribute("opponent", teamMap.get(game.otherTeam()).fullTeamName());

      switch (game.location()) {
        case HOME:
          pageContext.setAttribute("location", "Home");
          break;
        case AWAY:
          pageContext.setAttribute("location", "Away");
          break;
        case NEUTRAL:
          pageContext.setAttribute("location", "Neutral Site");
          break;
      }

      String resultString;
      if (game.won()) {
        resultString = "<noop class=\"win\">W</noop> ";
      } else {
        resultString = "<noop class=\"lose\">L</noop> ";
      }
      resultString += game.mainScore();
      resultString += " - ";
      resultString += game.otherScore();
      pageContext.setAttribute("result", resultString);
%>
  <tr>
    <td>${week}</td>
    <td>${fn:escapeXml(opponent)}</td>
    <td>${location}</td>
    <td>${result}</td>
  </tr>
<%
    }
%>
</table>
<%
  }
%>
</body>
</html>
