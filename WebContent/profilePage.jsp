<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<%@ page import = "java.util.SortedSet" %>
<%@ page import = "model.*" %>

<style>
	table, th, td {
	    border: 1px solid black;
	}
	
	th, td {
	    padding: 5px;
		text-align:center;
	}
</style>

<title></title>
</head>
<body>

<%
	HttpSession ses = request.getSession();
	String viewer = (String) ses.getAttribute("username");
	System.out.println("viewer is " + viewer);
	String pageOwner = request.getParameter("username");
	System.out.println("owner is " + pageOwner);
	ServletContext servletCon = getServletContext();
	AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");
	QuizManager qMan = (QuizManager) servletCon.getAttribute("quizManager");
	User u = accountMan.getUser(pageOwner);
	System.out.println("user is " + u);
%>

	<h2 id="name"><%=pageOwner%></h2>

	<div id="history" class="scroll" style="width: 50%; float: left">
		<h2>History</h2>
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
			%>
					<tr>
						<td>
							<%=qMan.getQuizName(s.getQuizID())%>
						</td>
				 		<td>
				 			<%=s.getTime().toString()%>
				 		</td>
				 		<td>
				 			<%=s.getUsedTime()%>
				 		</td>
				 		<td>
				 			<%=s.getNumCorrectAnswers()%>
				 		</td>
				 		<td>
				 			<%=s.getpoints()%>
				 		</td>
					</tr>
			<%
				}
			%>
		</table>
	</div>
	
	<%
		if (!viewer.equals(pageOwner)) {
	%>
			<div style="float:right">
			<% 
				if (!u.isFriend(viewer)) {
			%>
				<button id="addFriend" type="button">Send friend request</button>
				
			<% 
				} else {
			%>
				<button id="removeFriend" type="button">Remove friend</button>
				<button id="sendChallenge" type="button">Send challenge</button>
			<% 
				}
			%>
				<button type="button">Send Message</button>
				
				
			</div>
	<%		
		}
	%>
	
	
</body>
</html>