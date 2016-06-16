<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.google.simple.football.Game" %>
<%@ page import="com.google.simple.football.Season" %>
<%@ page import="com.google.simple.football.Team" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>

<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%
  List<Season> seasons = ObjectifyService.ofy()
      .load()
      .type(Season.class)
      .order("-season")
      .list();
  Set<String> seasonKeys = new HashSet<>();
  for (Season season : seasons) {
    seasonKeys.add(season.season());
  }

  // If the user supplies an invalid season key, the result will be the same as default behavior:
  // no data will be shown on the page.
  String seasonKey = request.getParameter("season");
  if (seasonKeys.contains(seasonKey)) {
    seasonKey = null;
  }
%>
<p>
  Choose a season:
  <select>
<%
  for (Season season : seasons) {
    pageContext.setAttribute("season", season.getName());
%>
    <option value="${season}">${season}</option>
<%
  }
%>
  </select>
</p>

</body>
</html>
