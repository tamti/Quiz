<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="databaseManagement.*"%>
<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
HttpSession ses = request.getSession();
String username = (String) session.getAttribute("username");
//System.out.println("username: "+username);
AccountManager accountman = new AccountManager();
User user = accountman.getUser(username);
if(user == null){
	response.sendRedirect("index.html");
}else{
	String quizName = request.getParameter("quizName");
	QuizManager man = new QuizManager();
	Quiz quiz = man.getQuiz(quizName);
	ses.setAttribute("quiz", quiz);
%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
		 <link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/>
 -->		<title><%=quiz.getQuizName() %></title>
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
	</head>
	<body>
	<a href="creatQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">

				<form action="search" method = "GET" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<a href="https://instagram.com/"><img class = "link" src="./img/2.png"></a>
				<p id = "bla" class="out"><a href="SignOutServlet">Sign Out</a></p>
				<p id = "bla" class = "boloshi"><a href="homepage.html"><%=user.getFirstName() %> <%=user.getLastName() %></a></p>
				<a href="homepage.html"><img class = "user" src="./img/user.jpg"></a>
				
			</div>
		</div>
		<div style="width: 100%">
    		<img style="float:right;" class = "animated bounce" src="./img/minuka.jpg">
		<h2>Name of Quiz: <%=quiz.getQuizName() %></h2>
		<h3>Description: <%=quiz.getDescription() %></h3>
		<h4>Creator: <a href="<%=quiz.getOwnerID()%>"><%=quiz.getOwnerID() %></a></h4>
		</div>
		<%-- <%
			for(int i=0; i<quiz.getQuestions().size(); i++){
				%>
				<fieldset class="question">
				<%if(quiz.getQuestions().get(i).getType()==Question.QuestionType.QUESTION_ANSWER){
						QuestinAnswerQuestion question = (QuestinAnswerQuestion) quiz.getQuestions().get(i);
					%>
					<input type="hidden" value="1" class="type">
					<p><%=question.getQuestion()%></p>
					<textarea rows="2" cols="100" class="questionAnswer"></textarea>
				
				<%
				} else if (quiz.getQuestions().get(i).getType()==Question.QuestionType.PICTURE_QUIZ){
						//PictureQuizQuestion question = (PictureQuizQuestion)quiz.getQuestions().get(i);	
				%>
					<input type="hidden" value="2" class="type">
					<img src="<%=question.getUrl() %>"></img>
					<textarea rows="2" cols="100" class="questionAnswer"></textarea>
				<%} else if(quiz.getQuestions().get(i).getType()==Question.QuestionType.MULTIPLE_CHOICE){
						//MultipleChoiceQuestion question = (MultipleChoiceQuestion)quiz.getQuestions().get(i);
					%> 
					
					<input type="hidden" value="3" class="type">
					<p><%=question.getQuestion()%></p>
					<select class="questionAnswer">
							<option value="A"><%=question.getAnswer1() %></option>
							<option value="B"><%=question.getAnswer2() %></option>
							<option value="C"><%=question.getAnswer3() %></option>
							<option value="D"><%=question.getAnswer4() %></option>
						</select>
				<% } else if(quiz.getQuestions().get(i).getType()==Question.QuestionType.FILL_THE_GAPS){
						FillTheGapsQuestion question = (FillTheGapsQuestion)quiz.getQuestions().get(i);
					%>
						<input type="hidden" value="4" class="type">
						<input type="hidden" value="<%=question.getAnswers().length %>" class="numAnswers">
						<p><%=question.getQuestion()%></p>
						<%for(int j=0; j<question.getAnswers().length; j++){ %>
							<input type = "text" class="answer<%=j %>"/>
						
						<%} %>
						
				<%} %> --%>
				</fieldset>
				<%
			}
			%>
			<button class = "but" onclick="submitQuiz(this)">OK</button>
	</body>
	<script>
	var bla;
	function submitQuiz(e){
		var questions = $(".question");
		var answers = [];
		console.log(questions);
		for(var i=0; i<questions.length; i++){
			var question = questions[i];
			console.log(question);
			bla = question;
			switch($(question).find(".type").val()){
				case "1":
				case "2":
				case "3":{
					console.log($(question).find(".type").val());
					answers.push($(question).find(".questionAnswer").val());
					break;
				}
				case "4":{
					var num = $(question).find(".numAnswers");
					var arr = [];
					for(var i=0; i<num; i++){
						arr.push($(question).find(".answer"+i));
					}
					answers.push(arr);
					break
				}
			}
		}
		$.ajax({
			url: "submitQuiz",
			type: "post",
			data: JSON.stringify(answers),
			success: function(data){
				bla = data;
				console.log(data);
				var d = JSON.parse(data);
				console.log(d);
				alert(d.value);
				window.location.replace(d.url);
			},
			error: function(e){
				alert("error");
			}
		});
	}
	</script><%-- 
	<%} %> --%>
</html>