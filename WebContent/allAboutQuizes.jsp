<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
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
		<div class = "headerMenu" size = "100">
			<div class = "search-box">

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
					
				</form>
				<div>
				<img class = "link" src="./img/2.png">
				<p> <a href="profilePage.jsp"><%=user.getFirstName()%> <%=user.getLastName() %></a>
				
				<p class="out" ><a href="SignOutServlet">Sign Out</a></p>
				</p>
				<a href=<%="profilePage.jsp?username=" + user.getUsername()%>><img class = "user" src="./img/user.png"></a>
				
				</div>
				
			</div>
		</div>
		<input class="btn btn-warning hell" style="width:33%; margin-top:1%" type="button" onclick="location.href='createQuiz.jsp'" value="Create Quiz" />

<div align="center">
<h1> Top Quizes </h1>
<div id="ann" class="scroll" style='overflow:scroll'>
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
<%} %>
</body>
</html>