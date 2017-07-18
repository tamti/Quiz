<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
<%@ page import="databaseManagement.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.SortedSet" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
		 <link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/>
 -->
 		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
 		
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<title>Summary</title>
</head>
<body>

<%
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");
System.out.println("username: "+username);

if(username == null){
	response.sendRedirect("homepage.jsp");
}else{
	AccountManager accountman = new AccountManager();
	User user = accountman.getUser(username);
	StatisticsDAO stat = new StatisticsDAO();
	String quizName = request.getParameter("quizname");
	//System.out.println("quizname: "+quizName);
	QuizManager man = new QuizManager();
	Quiz quiz = man.getQuiz(quizName);
	SortedSet<Statistics> st = stat.getStatisticsByQuiz(quiz.getID());
	ses.setAttribute("quiz", quiz);
%>

<a href="createQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<img class = "link" src="./img/2.png">
				<br>
				<p id = "bla" class = "boloshi"><a href=<%=user.getURL()%>><%=user.getFirstName() %> <%=user.getLastName() %></a></p>
				<a href=<%=user.getURL()%>><img class = "user" src="./img/user.png"></a>
				<p style="margin-top:2%" class="out" ><a href="SignOutServlet">Sign Out</a></p>
			</div>
		</div>
<div>

</div>

</body>
<%} %>
</html>