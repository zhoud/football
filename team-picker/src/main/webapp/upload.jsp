<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.google.simple.football.Game" %>
<%@ page import="com.google.simple.football.Season" %>
<%@ page import="com.google.simple.football.ServletMessages" %>
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
  List<Season> seasons = ObjectifyService.ofy()
      .load()
      .type(Season.class)
      .orderKey(true)
      .list();

  // Check that the season parameter is valid.
  String seasonString = request.getParameter("season");
  boolean seasonFound = false;
  for (Season season : seasons) {
    if (season.season().equals(seasonString)) {
      seasonFound = true;
      break;
    }
  }

  // If a season was selected, load the list of teams that have been added to that season.
  List<Team> teams = new ArrayList<Team>();
  if (seasonFound) {
    pageContext.setAttribute("selected_season", seasonString);
    teams = ObjectifyService.ofy()
        .load()
        .type(Team.class)
        .ancestor(Key.create(Season.class, seasonString))
        .order("fullTeamName")
        .list();
  } else {
    seasonString = null;
  }
%>
<body>

<%
  String messageCodeString = request.getParameter("message");
  String message = null;
  if (messageCodeString != null) {
    try {
      int messageCode = Integer.parseInt(messageCodeString);
      message = ServletMessages.getMessage(messageCode);
    } catch (NumberFormatException e) {
      // Leave message as null.
    }
  }

  if (message != null) {
    pageContext.setAttribute("message", message);
%>
<h3>${message}</h3>
<%
  }
%>
<h2>Add Season</h2>
<form action="/add_season" method="POST">
  <div>
    Season: <input type="text" name="season" /><br />
    <input type="submit" value="Submit" />
  </div>
</form>

<h2>Add Team</h2>
<form action="/add_team" method="POST">
  <div>
    Season:
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
    </select><br />
    Team full name: <input type="text" name="full_name" /><br />
    Team ID: <input type="text" name="team_id" /><br />
    <input type="submit" value="Submit" />
  </div>
</form>

<h2>Add Game</h2>
<form action="/upload.jsp" method="GET">
  <div>
    Season:
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
    </select>
    <input type="submit" value="Choose Season" /><br />
  </div>
</form>
<%
  // The game submission form doesn't appear unless a season is selected.
  if (seasonString != null) {
%>
<form action="/add_game" method="POST">
  <div>
    <input type="hidden" name="season" value="${fn:escapeXml(selected_season)}" />
    Season: ${fn:escapeXml(selected_season)}<br />
    Team ID:
    <select name="team_id">
<%
    for (Team team : teams) {
      pageContext.setAttribute("team", team.team());
      pageContext.setAttribute("team_name", team.fullTeamName());
%>
      <option value="${fn:escapeXml(team)}">${fn:escapeXml(team_name)}</option>
<%
    }
%>
    </select><br />
    Week:
    <select name="week">
<%
    for (int i = 1; i <= 13; i++) {
      pageContext.setAttribute("week_number", i);
%>
      <option value="${week_number}">${week_number}</option>
<%
    }
%>
    </select><br />
    Opposing team:
    <select name="other_team_id">
<%
    for (Team team : teams) {
      pageContext.setAttribute("other_team", team.team());
      pageContext.setAttribute("other_team_name", team.fullTeamName());
%>
     <option value="${fn:escapeXml(other_team)}">${fn:escapeXml(other_team_name)}</option>
<%
    }
%>
    </select><br />
    Location:
    <select name="location">
      <option value="home_game">Home</option>
      <option value="away_game">Away</option>
      <option value="neutral_site">Neutral Site</option>
    </select><br />
    Score (this team - other team):<br />
    <input type="text" name="team_score" /> - <input type="text" name="other_score" /><br />
    <input type="submit" value="Submit" />
  </div>
</form>
<%
  }
%>

</body>
</html>
