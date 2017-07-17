<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<meta name="viewport" content="width=device-width, initial-scale=1">
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
		System.out.println("viewer: " + viewer);
		String pageOwner = request.getParameter("username");

		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");
		QuizManager qMan = (QuizManager) servletCon.getAttribute("quizManager");
		System.out.print("page owner: " + pageOwner);
		User u = accountMan.getUser(pageOwner);
	%>

	<div data-role="page">
		<div data-role="header">
			<h1><%=pageOwner%></h1>
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
							%>
							<li>
								<a href=<%="profilePage.jsp?username=" + friend%>><%=friend%></a>
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
				<form class="bla" method="post" action="MessageServlet">
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

				<form id="tempform">
					<input type="hidden" name="requestType" value="addFriend"></input>
					<input type="hidden" name="fromUser" value="<%=viewer%>"></input> 
					<input type="hidden" name="toUser" value="<%=pageOwner%>"></input> 
					<input id="addFriend" type="submit" value="Send friend request"></input>
				</form>

				<%
					} else {
				%>
				<form class="bla" method="post" action="">
					<fieldset data-role="collapsible">
						<legend>Send challenge</legend>

						<input type="submit" value="Send"></input>
						<div data-role="controlgroup"></div>
					</fieldset>
				</form>

				<form method="post" action="FriendServlet">
					<input type="hidden" name="requestType" value="removeFriend"></input>
					<input type="hidden" name="fromUser" value="<%=viewer%>"></input> 
					<input type="hidden" name="toUser" value="<%=pageOwner%>"></input> 
					<input type="submit" value="Remove friend"></input>
				</form>
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
	
	<script>
		 $("#tempform").submit(function(e) {
			 console.log('submit');
			 e.preventDefault();
		//function sendFriendRequest() {
			var au = "FriendServlet";
		    $.ajax({
		           type: "POST",
		           url: au,
		           data: $("#addFriend").serialize(),
		           success: function(data)
		           {
		               alert(data);
		           }
		         });
		//}
		 });
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