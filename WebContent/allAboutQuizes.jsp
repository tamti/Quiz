<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
<%@ page import="databaseManagement.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Top Quizes</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%

	HttpSession ses = request.getSession();
	String username = (String) session.getAttribute("username");
	//System.out.println("username: "+username);
	AccountManager accountman = new AccountManager();
	User user = accountman.getUser(username);
	StatisticsDAO stat = new StatisticsDAO();
	QuizDAO quizdao = new QuizDAO();
	
	if(user == null)
		response.sendRedirect("login.html");
	else{
		//Message[] messages = DBHelper.getUserUnreadMessages(user.getUserID());
		//Challenge[] challenges = DBHelper.getUnseenChallenges(user);
		//FriendRequest[] friendRequest = DBHelper.getUnseenFriendRequest(user);
		Map<String, Integer> top10Quizes = stat.getTop10Quizzes();
		ArrayList<String> recentlyQuizes = quizdao.getQuizByDate();
		//Quiz[] recentCreatedQuizes = DBHelper.getRecentlyCreatedQuizes(user);
		//Quiz[] recentQuizActivities = DBHelper.getRecentQuizActivities(user);
		//Quiz[] userPlayedQuizes = DBHelper.getUserPlayedQuizes(user);
		//User[] search = DBHelper.searchUsers("");
%>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<link rel="stylesheet" type="text/css" href="./css/search.css"/>
<link rel="stylesheet" type="text/css" href="./css/styleHome.css"/>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<style>
body{
   background-image: url("topography.png");
}
a{
   color: orange;	
}
</style>

</head>
<body>
	<a href="createQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu">
			<div class = "search-box">

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text"  name="q" placeholder="Search ...">
					
				</form>
				
				<img class = "link" src="./img/2.png">
				<p> <a href="<%=user.getURL()%>"><%=user.getFirstName()%> <%=user.getLastName() %></a></p>
				
				<p style="margin-top:2%" class="out" ><a href="SignOutServlet">Sign Out</a></p>
				
				
				<a href=<%="profilePage.jsp?username=" + user.getUsername()%>><img class = "user" src="./img/user.png"></a>
				
				
			</div>
		</div>
		<input class="btn btn-warning hell" style="width:33%; margin-top:1%" type="button" onclick="location.href='createQuiz.jsp'" value="Create Quiz" />
<div style="display: flex;
    justify-content: space-around;">
	<div style="max-height:540px;
    overflow-y: auto;
    overflow-x: hidden;
    padding-right: 10px;">
	<div >
	<h1 style="margin-left:6%">All Quizes: </h1>
	<div id="ann" style="margin-left:2%" class="scroll" style='overflow:scroll'>
	</div>
	</div>
	<script>
	$(document).ready(function() {
		var output = document.getElementById('ann');
		$.get("QuizInfoServlet", function(responseJson) {
			$.each(responseJson, function(index, a) {
				 var ele = document.createElement("a");
				 ele.href = "quizPage.jsp?quizname="+a;
				 ele.append(a);
				 ele.append(document.createElement("br"));
				 output.appendChild(ele);
			});
		});
	});
	
	</script>
	</div>
	<div>
	
	<h1 > Top 10 Quizes: </h1>
	<div id="ann" style="margin-left:6%" class="scroll" style='overflow:scroll'>
	<%if(!top10Quizes.isEmpty()) {
		for(String name : top10Quizes.keySet()){ %>
					<div><a href="quizPage.jsp?quizname=<%=name%>"><%=name%></a>  <%top10Quizes.get(name);%></div>
				<%} %>
	<%} %>
	
	</div>
	</div>
	
	<div>
	<h1>Recently created Quizes:</h1>
	<div>
		<%if(recentlyQuizes.size()!=0) {
		for(int i=0; i<recentlyQuizes.size(); i++){ %>
					<div><a href="quizPage.jsp?quizname=<%=recentlyQuizes.get(i)%>"> <%=recentlyQuizes.get(i)%></a> </div> 
				<%} %>
	<%} %>
	
	</div>
	</div>
</div>



<%} %>
</body>
</html>