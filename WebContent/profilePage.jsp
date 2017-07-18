<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.Console"%>
<%@page import="servlets.*"%>
<%@ page import="model.*"%>
<%@ page import="databaseManagement.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
<link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/><!-- 
<link rel="stylesheet"
	href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script
	src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script> -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<%@ page import="java.util.SortedSet"%>
<%@ page import="model.*"%>

<style>
body {
	background-image: url("topography.png");
}

input::placeholder {
	color: orange !important;
}

a {
	color: orange !important;
}

.bla {
	width: 25%;
	display: inline-block;
	float: left;
}

table, th, td {
	border: 1px solid black;
}

th, td {
	padding: 5px;
	text-align: center;
}
</style>

<title></title>
</head>
<body>



	<%
		HttpSession ses = request.getSession();
		String viewer = (String) ses.getAttribute("username");
		
		String pageOwner = request.getParameter("username");

		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");
		QuizManager qMan = (QuizManager) servletCon.getAttribute("quizManager");
		User common = accountMan.getUser(viewer);
		User u = accountMan.getUser(pageOwner);
	%>

		<a href="createQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">
				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" style="margin-top:0%" type = "text" name="q" placeholder="Search ...">
				</form>
				<p style="position: absolute; width: 65px; height: 65px; margin-left: 800px; margin-top:-7%"><%=u.getUsername()%>'s page</p>
				<a href="homepage.jsp"><img class = "user" src="./img/user.png"></a>
				<p style="margin-top:2%" class="out" ><a href="SignOutServlet">Sign Out</a></p>
				
			</div>
		</div>
		<div data-role="main" class="ui-content">
			<div class="top">
				<fieldset class="bla" data-role="collapsible">
					<legend>Friend list</legend>
					<div id="peoplediv" style="max-height: 250px; overflow-y: auto; overflow-x: hidden; padding-right: 10px;">
						<ul id="peopleUL">
							<%
								SortedSet<String> friends = u.getFriends();

								for (String friend : friends) {
							%>
							<li><a href=<%="profilePage.jsp?username=" + friend%>><%=friend%></a>
							</li>
							<%
								}
							%>
						</ul>
					</div>
					<div data-role="controlgroup"></div>
				</fieldset>

				<%
					if (!viewer.equals(pageOwner)) {
				%>
				<form id="sendmsg" class="bla" onsubmit="sendMSG(event)">
					<fieldset data-role="collapsible">
						<legend>Send message</legend>
						<textarea id="msgarea" name="msg" rows="" cols=""
							placeholder="Write your message here"></textarea>
						<input type="hidden" name="fromUser" value="<%=viewer%>"></input>
						<input type="hidden" name="toUser" value="<%=pageOwner%>"></input>
						<input type="submit" value="Send"></input>
						<div data-role="controlgroup"></div>
					</fieldset>
				</form>
				<%
					if (!u.isFriend(viewer)) {
				%>

				<form id="addfriend" onsubmit="sendFriendRequest(event)">
					<input type="hidden" name="requestType" value="addFriend"></input>
					<input type="hidden" name="fromUser" value="<%=viewer%>"></input> <input
						type="hidden" name="toUser" value="<%=pageOwner%>"></input> <input
						type="submit" value="Send friend request"></input>
				</form>

				<%
					} else {
				%>

				<form id="removefriend" onsubmit="removeFriend(event)">
					<input type="hidden" name="requestType" value="removeFriend"></input>
					<input type="hidden" name="fromUser" value="<%=viewer%>"></input> <input
						type="hidden" name="toUser" value="<%=pageOwner%>"></input> <input
						type="submit" value="Remove friend"></input>
				</form>

				<%
					}
				%>

			</div>
			<%
				} else {
					SortedSet<Message> messages = accountMan.getMessagesOf(u.getID());
			%>
			<div style="display: flex; justify-content: space-around;">
				<div style="max-height: 540px; overflow-y: auto; overflow-x: hidden; padding-right: 10px;">
					<h3>Messages</h3>
					<%
					for (Message msg : messages) {
						String sender = accountMan.getUserById(msg.getSender());
				%>
					<p><strong>Message: </strong><%=msg.getText()%></p>
					<p><strong><%="Message from: "%></strong>
						<a href=<%="profilePage.jsp?username=" + sender%>><%=sender%></a>
						<strong><%=" sent on " + msg.getTime()%></strong></p>
					<hr>
					<br>
				<%
					}
				%>
				</div>
				
				<div style="max-height: 540px; overflow-y: auto; overflow-x: hidden; padding-right: 10px;">
					<h3>Challenges</h3>
					<%
					
					SortedSet<Challenge> challenges = accountMan.getChallengesOf(u.getID());
					
					for (Challenge chal : challenges) {
						String sender = accountMan.getUserById(chal.getSender());
				%>
					<% 
						String note = chal.getText();
						if (!note.isEmpty()) {
					%>
							<p><strong>Note: </strong><%=note%></p>
					<%
						}
						int challengeID = chal.getChallengeID();
						int chalQuizID = chal.getQuiz();
						String chalQuizName = qMan.getQuizName(chalQuizID);
						Quiz q = qMan.getQuiz(chalQuizName);
					%>
					<p><strong>Challenge for quiz: </strong><a href=<%=q.getURL()%>><%=q.getQuizName()%></a>
						<strong><%=" from: "%></strong>
						<a href=<%="profilePage.jsp?username=" + sender%>><%=sender%></a>
						<strong><%=" sent on " + chal.getTime()%></strong></p>
						<% if (!chal.challengeSeen()) {%>
							<form id="accpetchal" action="ChallengeServlet">
								<input type="hidden" name="challengeID" value="<%=challengeID%>"></input>
								<input type="hidden" name="URL" value="<%=q.getURL()%>"></input>
								<input type="hidden" name="response" value="accept"></input> 
								<input type="submit" value="Accept challenge"></input>
							</form>
							<form id="ignorechal" onsubmit="ignoreChallenge(event)">
								<input type="hidden" name="challengeID" value="<%=challengeID%>"></input>
								<input type="hidden" name="URL" value="<%="profilePage.jsp?username=" + pageOwner%>"></input>
								<input type="hidden" name="response" value="ignore"></input> 
								<input type="submit" value="Ingore challenge"></input>
							</form>
						<% 
							}  else if (chal.challengeAccepted()) {
						%>
							<p><strong>Challenge accepted</strong></p>
						<%	
							} else { 
						%>
							<p><strong>Challenge ignored</strong></p>
						<% 
							} 
						%>
						<hr>
					<br>
				<%
					}
				%>
				</div>
				
				<%
				}
			%>

				<div id="history" class="scroll" style="width: 50%; clear: both">
					<h3><%=pageOwner + "'s history"%></h3>
					<table id="historyTable" style='overflow: scroll'>
						<tr>
							<th>Quiz</th>
							<th>Taken on</th>
							<th>Used time</th>
							<th>Correct answers</th>
							<th>Earned points</th>
						</tr>
						<%
						SortedSet<Statistics> stats = u.getStats();
						for (Statistics s : stats) {
							String quizName = qMan.getQuizName(s.getQuizID());
					%>
						<tr>
							<td><a href=<%="quizPage.jsp?quizname=" + quizName%>> <%=quizName%>
							</a></td>
							<td><%=s.getTime().toLocalDateTime()%></td>
							<td><%=s.getUsedTime()%></td>
							<td><%=s.getNumCorrectAnswers()%></td>
							<td><%=s.getpoints()%></td>
						</tr>
						<%
						}
					%>
					</table>
				</div>
			</div>
			</div>
		</div>

	<script>
		function sendFriendRequest(e) {
			e.preventDefault();
			var au = "FriendServlet";
			$.ajax({
				type : "POST",
				url : au,
				data : $("#addfriend").serialize(),
				headers : {
					'content-type' : 'application/x-www-form-urlencoded'
				}
			});
		}
	</script>

	<script>
		function removeFriend(e) {
			e.preventDefault();
			var au = "FriendServlet";
			$.ajax({
				type : "POST",
				url : au,
				data : $("#removefriend").serialize(),
				headers : {
					'content-type' : 'application/x-www-form-urlencoded'
				}
			});
		}
	</script>

	<script>
		function sendMSG(e) {
			e.preventDefault();
			var au = "MessageServlet";
			$.ajax({
				type : "POST",
				url : au,
				data : $("#sendmsg").serialize(),
				headers : {
					'content-type' : 'application/x-www-form-urlencoded'
				}
			});
			$("#msgarea").val('');
		}
	</script>
	
	<script>
		function ignoreChallenge(e) {
			e.preventDefault();
			var au = "ChallengeServlet";
			$.ajax({
				type : "POST",
				url : au,
				data : $("#ignorechal").serialize(),
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
</html>