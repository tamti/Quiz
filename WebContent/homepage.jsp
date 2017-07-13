<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello <% out.print(request.getSession().getAttribute("username")); %></title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

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

				
<div align="center" id = "cover">
 <input  class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='createQuiz.html'" value="All about quizes" />
 <input class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='friends.html'" value = "Friends">
 <input  class="btn btn-warning hell" style="width:25%" type="button" value = "Announcements" />
 <input class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='friends.html'" value = "Sign out" />
</div>		
 <form align="left" method="post" action="RegistrationServlet" type="text/css" style="margin-top:1%; ">
 		  <p>Welcome <% out.print(request.getSession().getAttribute("username")); %> !</p>
 </form>

<div id="history" class="scroll">
	<table id="historyTable" style='width:55%; overflow:scroll'>
		<caption>History</caption>
		<tr>
			<th>Quiz</th>
			<th>Taken on</th>
			<th>Used time</th>
			<th>Correct answers</th>
			<th>Earned points</th>
		</tr>
	</table>
</div>

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
</script>

</body>
</html>