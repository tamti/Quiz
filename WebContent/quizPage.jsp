<%@page import="others.quizListener"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="databaseManagement.*"%>
<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");
System.out.println("username: "+username);
AccountManager accountman = new AccountManager();
User user = accountman.getUser(username);
if(user == null){
	response.sendRedirect("homepage.html");
}else{
	String quizName = request.getParameter("quizname");
	System.out.println("quizname: "+quizName);
	QuizManager man = new QuizManager();
	Quiz quiz = man.getQuiz(quizName);
	ses.setAttribute("quiz", quiz);
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
		 <link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/>
 -->		<title><%=quiz.getQuizName() %></title>
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
	</head>
	<body>
	<a href="creatQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">

				<form action="search" method = "GET" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<a href="https://instagram.com/"><img class = "link" src="./img/2.png"></a>
				<p id = "bla" class="out"><a href="SignOutServlet">Sign Out</a></p>
				<p id = "bla" class = "boloshi"><a href="homepage.html"><%=user.getFirstName() %> <%=user.getLastName() %></a></p>
				<a href="homepage.html"><img class = "user" src="./img/user.jpg"></a>
				
			</div>
		</div>
		<div style="width: 100%">
    		<img style="float:right;" class = "animated bounce" src="./img/minuka.jpg">
		<h2>Name of Quiz: <%=quiz.getQuizName()%></h2>
		<h3>Description: <%=quiz.getDescription() %></h3>
		<h4>Creator: <%=accountman.getUserById(quiz.getOwnerID())%></h4>
		<h4>number of questions: <%=quiz.getQuestions().size()%></h4>
		 <% for(int i = 0;i< quiz.getQuestions().size();i++){ 
			 System.out.println(quiz.getQuestions().get(i));%>
			  <p> <%=(quiz.getQuestions().get(i))%></p>
			<% }%>
		 
		</div>
		<%} %>
		<div>
		
		</div>
</html>