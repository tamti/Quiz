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
 		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
 		
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<title>Summary</title>

<style>
	.searchResults {
	  position:fixed;
	  top:7%;
	  background-color: white; 
	  border: 1px solid black; 
	  z-index: 50;
	  display: none;
	}

	body {
   		background-image: url("topography.png");
	}

	table, th, td {
		border: 1px solid black;
	}
	
	th, td {
		padding: 5px;
		text-align: center;
	}
</style>

</head>
<body>

<%
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");

if(username == null) {
	response.sendRedirect("homepage.jsp");
} else {
	AccountManager accountman = new AccountManager();
	User user = accountman.getUser(username);
	StatisticsDAO stat = new StatisticsDAO();
	String quizName = request.getParameter("quizname");

	QuizManager man = new QuizManager();
	Quiz quiz = man.getQuiz(quizName);
	SortedSet<Statistics> st = stat.getStatisticsByQuiz(quiz.getID());
	ses.setAttribute("quiz", quiz);
	
	SortedSet<Statistics> userScores = stat.getStatisticsOfUserForQuiz(user.getID(), quiz.getID());
	Statistics userHighestScore = userScores.first();
	String message = "I scored " + userHighestScore.getpoints() + " points in " + userHighestScore.getUsedTime() + " seconds. Beat me if you can!";
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

		<div id="scoreboard" align="center">
					<h2>Quiz: <%=quiz.getQuizName()%></h2>
					
				<hr>
				<h3>Your scores for this quiz</h3>
				<table id="userScores" style='overflow: scroll'>
					<tr>
						<th>Score</th>
						<th>Used time</th>
						<th>Taken on</th>
					</tr>
				<%
					for (Statistics s : userScores) {
				%>
					<tr>
						<td><%=s.getpoints()%></td>
						<td><%=s.getUsedTime()%></td>
						<td><%=s.getTime().toLocalDateTime()%></td>
					</tr>
				<%
					}
				%>
  				</table>
  					
  					<hr>
					<legend>Challenge your friends</legend>
					<form id="sendChall" onsubmit="sendChallenge(event)">
						<input type="hidden" name="requestType" value="sendchal"></input>
						<input type="hidden" name="quizID" value="<%=quiz.getID()%>"></input>
						<input type="hidden" name="fromUser" value="<%=username%>">
						</input> <input type="hidden" name="msg" value="<%=message%>"></input>
						<input type="text" name="toUser" placeholder="Type in a username" title="Type in a username"></input>
						<input type="submit" value="send"></input>
					</form>
  					
				<hr>
				<h3>Top 10 scores</h3>
				<table id="topScores" style='overflow: scroll'>
					<tr>
						<th>NO</th>
						<th>Username</th>
						<th>Used time</th>
						<th>Score</th>
						<th>Taken on</th>
					</tr>
				<%
					int no = 1;
					for (Statistics s : st) {
						if (no > 10) {
							break;
						}
						int userID = s.getUserID();
						String scoreOf = accountman.getUserById(userID);
				%>
					<tr>
						<td><%=no%></td>
						<td>
							<a href=<%="profilePage.jsp?username=" + scoreOf%>><%=scoreOf%></a>
						</td>
						<td><%=s.getUsedTime()%></td>
						<td><%=s.getpoints()%></td>
						<td><%=s.getTime().toLocalDateTime()%></td>
					</tr>
				<%
						no++;
					}
				%>
				</table>
		</div>
	
	<script>
		function sendChallenge(e) {
			e.preventDefault();
			var au = "ChallengeServlet";
			$.ajax({
				type : "POST",
				url : au,
				data : $("#sendChall").serialize(),
				headers : {
					'content-type' : 'application/x-www-form-urlencoded'
				},
				success : function(data) {
					location.reload();
				}
			});
		}
	</script>

</body>
<%} %>
</html>