<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.AccountManager"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<%
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");
//System.out.println("username: "+username);
AccountManager accountman = new AccountManager();
User user = accountman.getUser(username);
if(user == null){
	response.sendRedirect("login.html");
}else{
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
		 <link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/>
 -->
 		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
 		
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
		
		<title>Quiz Web Site</title>
		
	</head>
	<body>
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
				
			</div>
		</div>

<style>
body{
   background-image: url("topography.png");
}

table, th, td {
    border: 1px solid black;
}

th, td {
    padding: 5px;
	text-align:center;
}

</style>
</head>
<body>

				
<div align="center" id = "cover" style="margin-top:1%">
 <input  class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='<%=user.getURL()%>'" value="Profile page" />
 <input  class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='allAboutQuizes.jsp'" value="All about quizes" />
 	<%if(accountman.isAdminAcc(username)){%>
	 	<input  class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='adminpage.html'" value="ADMIN PAGE" />
	<%} %>
 	<input class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='SignOutServlet'" value = "Sign out" />
</div>

<section style ="width:100%">
<div id="history" class="scroll" style ="width:50%; float:left">
	<table id="historyTable" style='overflow:scroll'>
		<h1>Recent activity</h1>
		<tr>
			<th>Quiz</th>
			<th>Taken on</th>
			<th>Used time</th>
			<th>Correct answers</th>
			<th>Earned points</th>
		</tr>
	</table>
</div>

<div style ="width:50%; float:right">
<h1> Announcements </h1>
<div id="ann" class="scroll" style='overflow-y:scroll'>
</div>
</div>
</section>

<script>
	$(document).ready(function() {
		$.get("HistoryServlet", function(responseJson) {
			$.each(responseJson, function(index, s) {
				$("<tr>").appendTo($("#historyTable"))
					.append($("<td>").text(s.quizID))
					.append($("<td>").text(s.takenOn))
					.append($("<td>").text(s.usedTimeInSeconds))
					.append($("<td>").text(s.numCorrectAnswers))
					.append($("<td>").text(s.numEarnedPoints));
			});
		});
	});
	
	$(document).ready(function() {
		var output = document.getElementById('ann');
		$.get("AnnouncementServlet", function(responseJson) {
			var i=1;
		    var val="";
			$.each(responseJson, function(index, a) {
				 var ele = document.createElement("div");
				 ele.append("From : "+a.username);
				 ele.append(" Sent At : "+a.sentOn);
				 ele.append(document.createElement("br"));
				 ele.append(a.txt);
				 output.appendChild(ele);
			});
		});
	});
	

	
	
</script>
<%} %>
</body>
</html>