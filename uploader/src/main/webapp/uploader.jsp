<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%
  String errorString = request.getParameter("error");
  if (errorString != null) {
%>
<div>
  <p>Please provide a valid season.</p>
</div>
<%
  }
%>
<form action="/upload" method="POST">
  <div>
    Upload a season: <input type="text" name="season" /><br />
    <input type="submit" value="Upload" />
  </div>
</form>
</body>
</html>
