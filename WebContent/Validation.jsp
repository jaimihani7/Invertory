<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
String loginUser=(String)session.getAttribute("userName");
String loginpassword=(String)session.getAttribute("userPassword");
String loginMobileNumber=(String)session.getAttribute("userMobileNumber");
String mgs;
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<title>Mobile Number Validation</title>
		<meta name="description" content="Hound is a Dashboard & Admin Site Responsive Template by hencework." />
		<meta name="keywords" content="admin, admin dashboard, admin template, cms, crm,  admin templates, responsive admin, sass, panel, software, ui, visualization, web app, application" />
		<meta name="author" content="hencework"/>
</head>
<body>
  <input type="search" value="text">
  <button name="Validation" value="Serach" id="Validation"></button>
</body>
</html>