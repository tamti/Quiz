<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Top Quizes</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
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

</body>
</html>