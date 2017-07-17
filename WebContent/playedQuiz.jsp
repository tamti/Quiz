<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
<%@ page import="databaseManagement.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");
System.out.println("username: "+username);
AccountManager accountman = new AccountManager();
User user = accountman.getUser(username);
StatisticsDAO stat = new StatisticsDAO();
if(user == null){
	response.sendRedirect("homepage.jsp");
}else{
	String quizName = request.getParameter("quizname");
	//System.out.println("quizname: "+quizName);
	QuizManager man = new QuizManager();
	Quiz quiz = man.getQuiz(quizName);
	//stat.getStatisticsByQuiz(quizID)
	ses.setAttribute("quiz", quiz);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
<%} %>
</html>