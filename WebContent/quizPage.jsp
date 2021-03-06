<%@page import="others.quizListener"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="databaseManagement.*"%>
<%@page import="others.QuestionType"%>
<%@page import="java.util.ArrayList"%>

<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	HttpSession ses = request.getSession();
	String username = (String) session.getAttribute("username");
	System.out.println("username: " + username);
	AccountManager accountman = new AccountManager();
	User user = accountman.getUser(username);
	if (user == null) {
		response.sendRedirect("homepage.jsp");
	} else {
		String quizName = request.getParameter("quizname");
		System.out.println("quizname: " + quizName);
		QuizManager man = new QuizManager();
		Quiz quiz = man.getQuiz(quizName);
		ses.setAttribute("quiz", quiz);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="./css/Quiz.css" />
<link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<!-- 		<link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/>
 -->
<title><%=quiz.getQuizName()%></title>
<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
</head>
<body>
	<a href="creatQuiz.jsp"><img class="quiz" src="./img/blaa.png"
		title="Create New Quiz"></a>
	<hr>
	<div class="headerMenu" size="60">
		<div class="search-box">

			<form action="searchServlet" method="POST" id="search">
				<input id="searchForm" type="text" name="q" size="60"
					placeholder="Search ...">
			</form>
			<a href="https://instagram.com/"><img class="link"
				src="./img/2.png"></a>
			<p id="bla" class="out">
				<a href="SignOutServlet">Sign Out</a>
			</p>
			<p id="bla" class="boloshi">
				<a href="homepage.jsp"><%=user.getFirstName()%> <%=user.getLastName()%></a>
			</p>
			<a href="homepage.jsp"><img class="user" src="./img/user.png"></a>

		</div>
	</div>
	<div style="width: 100%">
		<img style="float: right;" class="animated bounce"
			src="./img/minuka.jpg">
		<h2>
			Name of Quiz:
			<%=quiz.getQuizName()%></h2>
		<h3>
			Description:
			<%=quiz.getDescription()%></h3>
		<h4>
			Creator:
			<%=accountman.getUserById(quiz.getOwnerID())%></h4>
		<h4>
			number of questions:
			<%=quiz.getQuestions().size()%></h4>

		<%
			ArrayList<Question> questions = quiz.getQuestions();
				System.out.println("aa " + questions.size());
				if (!questions.isEmpty()) {
					for (int i = 0; i < questions.size(); i++) {
		%>
		<div class="question">
			<p>
				<%=(questions.get(i).getQuestionStr())%>
			</p>
			<%
				System.out.println(quiz.isOnePage());
							QuestionType currType = questions.get(i).getType();
							if (currType.equals(QuestionType.basicResponse)) {
			%>
			<input type="hidden" value="1" class="type">
			<textarea rows="2" cols="15" class="questionAnswer"></textarea>
			<%
				} else if (currType.equals(QuestionType.fillInTheBlank)) {
			%><input type="hidden" value="4" class="type"> <input
				type="hidden" class="numAnswers"
				value=<%=questions.get(i).getAnswers().size()%>>
			<%
				for (int j = 0; j < questions.get(i).getAnswers().size(); j++) {
			%>
			<textarea rows="2" cols="15" class="answ<%=j%>"></textarea>
			<%
				}

							} else if (currType.equals(QuestionType.multipleChoice)) {
			%>
			<input type="hidden" value="2" class="type"> <select
				class="questionAnswer">
				<option value="A">
					<%=questions.get(i).getAnswers().get(0).getAnswerStr()%></option>
				<option value="B">
					<%=questions.get(i).getAnswers().get(1).getAnswerStr()%></option>
				<option value="C">
					<%=questions.get(i).getAnswers().get(2).getAnswerStr()%></option>
				<option value="D">
					<%=questions.get(i).getAnswers().get(3).getAnswerStr()%></option>
			</select>
			<%
				} else if (currType.equals(QuestionType.pictureResponse)) {
								System.out.println(questions.get(i).getPhoto());
			%>
			<input type="hidden" value="3" class="type"> <img
				src="<%=questions.get(i).getPhoto()%>" width="100px" height="100px"></img>
			<textarea rows="2" cols="15" class="questionAnswer"></textarea>
			<%
				}
			%>
		</div>
		<%
			}
		%>

		<%
			}
			}
		%>
		<input class="btn btn-warning hell"  style = "top-margin:5%"type="submit" value="submit" onclick="submitQuiz(this)">
	</div>
	<script>
		$('input[type="checkbox"]').on('change', function() {
			$(this).siblings('input[type="checkbox"]').prop('checked', false);
		});

		function submitQuiz(e) {
			var questions = $(".question");
			var answers = [];
			var been = false;
	
			for (var i = 0; i < questions.length; i++) {
				var question = questions[i];
				switch ($(question).find(".type").val()) {
				case "1": {
					if ($(question).find(".questionAnswer").val() === "") {
						alert("Please don't leave little gap empty!");
					}
					answers.push($(question).find(".questionAnswer").val());
					break;
				}
				case "2": {
					answers.push($(question).find(".questionAnswer option:selected").text());
					break;

				}
				case "3": {
					if ($(question).find(".questionAnswer").val() === "") {
						alert("Please don't leave little gap empty!");
					}
					answers.push($(question).find(".questionAnswer").val());
					break;
				}
				case "4": {
					var num = $(question).find(".numAnswers").val();
					var arr = [];
					for (var j = 0; j < num; j++) {
						if (($(question).find(".answ" + j).val()) === "") {
							alert("Please don't leave little gap empty!");
						}
						answers.push($(question).find(".answ" + j).val());
					}
					break;
				}
				}

			}
			for(var h = 0;h<answers.length;h++){
				alert(answers[h])
			}

			$.ajax({
				url : "submitQuizServlet",
				type : "post",
				data : JSON.stringify(answers),
				headers : {
					'content-type' : 'application/json'
				},
				success : function(data) {
					//bla = data;
					console.log(data);
					var d = JSON.parse(data);//???
					console.log(d);
					//alert(d.value);
					window.location.replace(d.url);//linkze gadasvla
				},
				error : function(e) {
					alert("error");
				}
			});
		}
	</script>
</body>
</html>