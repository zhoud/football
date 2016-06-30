<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%
  String successString = request.getParameter("success");
  String errorString = request.getParameter("error");
  if (successString != null) {
%>
<div>
  <p>Successfully uploaded games!</p>
</div>
<%
  }
  if (errorString != null) {
%>
<div>
  <p>Please provide a valid season.</p>
</div>
<%
  }
%>
<h4>Upload games from an entire season</h4>
<form action="/upload" method="POST">
  <div>
    Season: <input type="text" name="season" /><br />
    <input type="submit" value="Upload" />
  </div>
</form>
<h4>Upload games from a week</h4>
<form action="/upload_week" method="POST">
  <div>
    Season: <input type="text" name="season" />
    Week: <input type="text" name="week" />
    <br />
    <input type="submit" value="Upload" />
  </div>
</form>
<h4>Upload games for a team</h4>
<form action="/upload_team" method="POST">
  <div>
    Season: <input type="text" name="season" />
    Full Team Name: <input type="text" name="team_name" />
    Team ID: <input type="text" name="team" />
    <br />
    <input type="submit" value="Upload" />
  </div>
</form>
</body>
</html>
