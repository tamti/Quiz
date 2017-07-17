<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>

<%

	HttpSession ses = request.getSession();
	String username = (String) session.getAttribute("username");
	//System.out.println("username: "+username);
	AccountManager accountman = new AccountManager();
	User user = accountman.getUser(username);
	
	if(user == null)
		response.sendRedirect("login.html");
	else{
		//Message[] messages = DBHelper.getUserUnreadMessages(user.getUserID());
		//Challenge[] challenges = DBHelper.getUnseenChallenges(user);
		//FriendRequest[] friendRequest = DBHelper.getUnseenFriendRequest(user);
		//Quiz[] popQuizes = DBHelper.getPopularQuizes();
		//Quiz[] recentCreatedQuizes = DBHelper.getRecentlyCreatedQuizes(user);
		//Quiz[] recentQuizActivities = DBHelper.getRecentQuizActivities(user);
		//Quiz[] userPlayedQuizes = DBHelper.getUserPlayedQuizes(user);
		//User[] search = DBHelper.searchUsers("");
%>
	<head>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/challenge.css"/>
 -->		<link rel="stylesheet" type="text/css" href="./css/search.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/transi.css"/> 
 -->		<title>search</title>
		<link rel="stylesheet" type="text/css" href="./css/styleHome.css"/>
		<!-- <link rel="stylesheet" type="text/css" href="./css/animate.css"/> -->
<!-- 		<link rel="stylesheet" type="text/css" href="./css/css/bootstrap.min.css"/>
 -->		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
		
	</head>
<body>
<a href="createQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<img class = "link" src="./img/2.png"></a>
				<p class="out"><a href="SignOutServlet">Sign Out</a></p>
				<p class = "boloshi"><a href="homepage.jsp"><%=user.getFirstName() %> <%=user.getLastName() %></a></p>
				<a href="homepage.jsp"><img class = "user" src="./img/user.png"></a>
				
			</div>
		</div>

	<fieldSet>
		<div style="width: 100%">
    		<img style="float:right;" class = "animated bounce" src="./img/giphy (1).gif">
		<legend>Result:</legend>
		<br>
		<br>
		<br>
		<h2>USERS:</h2>
		<%
			User[] users = (User[]) request.getAttribute("users");
			if (users.length == 0) {
		%>
		<h6>there is no User</h6>
		<img class = "animat" src="">
		<%
			}
			for (int i = 0; i < users.length; i++) {
		%>

		<h6>
			<a href="<%=users[i].getURL()%>"> <%= users[i].getFirstName() %> <%= users[i].getLastName()%></a>
		</h6>
		<%
			}
		%>
		<h5>
		<br>
		<br>
		<h2>QUIZES:</h2>
		<br>
		
		<%Quiz[] quizes = (Quiz[])request.getAttribute("quizes");
			if(quizes.length == 0){
		%>
		<h6>there is no Quiz</h6>
		<%}
			for(int i = 0; i<quizes.length; i++){
			%>
		<br>
		<h6>
			<a href="<%=quizes[i].getURL()%>"> <%= quizes[i].getQuizName() %></a>
		</h6>
		<%} %>
		
		<br>
		<br>
		<br>
			<a href="homePage.jsp">RETURN HOMEPAGE</a>
		</h5>
		</div>
	</fieldSet>

</body>
<%} %>
</html>