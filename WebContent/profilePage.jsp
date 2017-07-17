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
<link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
<link rel="stylesheet"
	href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
<script
	src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
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
					<input id="searchForm" style="margin-top:-2%" type = "text" name="q" placeholder="Search ...">
				</form>
				<p id = "bla" class = "boloshi"><%=u.getUsername()%>'s page</p>
				<a href="<%=common.getURL()%>"><img class = "user" src="./img/user.png"></a>
				<p style="margin-top:2%" class="out" ><a href="SignOutServlet">Sign Out</a></p>
				
			</div>
		</div>
		<div data-role="main" class="ui-content">
			<div class="top">
				<fieldset class="bla" data-role="collapsible">
					<legend>Friend list</legend>
					<input type="text" id="search" onkeyup="filter()"
						placeholder="Search..." title="Type in a name">
					<div id="peoplediv">
						<ul id="peopleUL">
							<%
								SortedSet<String> friends = u.getFriends();

								for (String friend : friends) {
									System.out.println("friend : " + friend);
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
						<textarea name="msg" rows="" cols=""
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
			console.log('submit');
			e.preventDefault();
			var au = "FriendServlet";
			console.log()
			$.ajax({
				type : "POST",
				url : au,
				data : $("#removefriend").serialize(),
				headers : {
					'content-type' : 'application/x-www-form-urlencoded'
				},
				success : function(data) {
					alert(data);
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
		}
	</script>

	<script>
		function filter() {
			var input, filter, ul, li, a, i;
			input = document.getElementById("search");
			filter = input.value.toUpperCase();
			ul = document.getElementById("peopleUL");
			li = ul.getElementsByTagName("li");
			for (i = 0; i < li.length; i++) {
				a = li[i].getElementsByTagName("a")[0];
				if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
					li[i].style.display = "";
				} else {
					li[i].style.display = "none";
				}
			}
		}
	</script>

</body>
</html>