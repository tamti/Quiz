<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.AccountManager"%>
<%@ page import="model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 --%>
<head>
	<meta charset="ISO-8859-1">
	<title>Create new Quiz</title>
			 <link rel="stylesheet" type="text/css" href="./css/Quiz.css"/>
			 <link rel="stylesheet" type="text/css" href="./css/HeaderSCC.css"/>
			 
	<!-- <link rel="stylesheet" type="text/css" href="./css/styleHome.css"/> 
 -->		 
	<!-- <link rel="stylesheet" type="text/css" href="./css/styleHome.css"/> -->
	<!-- <link rel="stylesheet" type="text/css" href="./css/bootstrap/css/bootstrap.min.css"/> -->
	<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
</head>
<body>	 
		 
<a href="createQuiz.jsp"><img class = "quiz" src="./img/blaa.png" title="Create New Quiz"></a>
			<hr>
		<div class = "headerMenu" size = "60">
			<div class = "search-box">

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<img class = "link" src="./img/2.png"></a>
				<p id = "bla" class="out"><a href="SignOutServlet">Sing Out</a></p>
			 <p id = "bla" class = "boloshi"><a href="homepage.jsp"><%=user.getFirstName()%> <%=user.getLastName() %></a></p>
				<a href="homepage.jsp"><img class = "user" src="./img/user.png"></a>
				
			</div>
		</div>
		 
<div id="questionTemplate" style="display: none">
	<fieldset class="question">
		<div class="questionType">
			Question Type:
			<select class="questionTypeSelect">
				<option value="questionAnswerTemplate">Question Answer</option>
				<option value="multipleChoiceTemplate">Multiple Choice</option>
				<option value="pictureQuestionTemplate">Picture Question</option>
				<option value="fillTheGapsTemplate">Fill the gaps</option>
			</select>
			<button class = "but" onclick="removeQuestion(this)">Remove</button>
		</div>
		<div class="questionBody">
			<h6>Enter Question</h6>
			<textarea rows="2" cols="80" class="questionText"></textarea>
			<h6>Enter correct answer</h6>
			<textarea rows="2" cols="80" class="questionAnswer"></textarea>
			<p>Enter Max Point:
			<input type="number" id="MaxPoint" class = "maxpoint"/></p>
			
		</div>
	</fieldset>
</div>
<div id="questionAnswerTemplate" style="display: none">
	<h6>Enter Question</h6>
	<textarea rows="2" cols="80" class="questionText"></textarea>
	<h6>Enter correct answer</h6>
	<textarea rows="2" cols="80" class="questionAnswer"></textarea>
	<p>Enter Max Point:
	<input type="number" id="MaxPoint" class = "maxpoint"/></p>
	
</div>
<div id="multipleChoiceTemplate" style="display: none">
	<h6>Enter Question</h6>
	<textarea rows="5" cols="100" class="questionText"></textarea>
	<h6>Enter Max Point:</h6>
	<input type="number" id="MaxPoint" class = "maxpoint"/>
	<p>A: <input type="text" class="choice1"></p>
	<p>B: <input type="text" class="choice2"></p>
	<p>C: <input type="text" class="choice3"></p>
	<p>D: <input type="text" class="choice4"></p>
	<p>
		Correct Answer: <select class="correctAnswer">
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="C">C</option>
							<option value="D">D</option>
						</select>
						</p>
	
</div>
<div id="pictureQuestionTemplate" style="display: none">
	<h6>Enter picture url:</h6>
	<input type="text" class="pictureURL" onchange="changePictureURL(this)"/>
	<img class="pqImage" width="100px" height="100px"/>
	<h6>Enter Question:</h6>
	<textarea rows="5" cols="100" class="questionText"></textarea>
	<h6>Enter correct answer</h6>
	<textarea rows="2" cols="100" class="pictureAnswer"></textarea>
	<h6>Enter Max Point:</h6>
	<input type="number" id="MaxPoint" class = "maxpoint"/>
</div>
<div id="fillTheGapsTemplate" style="display: none">
	<h6>Enter Question</h6>
	<textarea rows="5" cols="100" class="questionText"></textarea>
	<h6>Enter answers quantity</h6>
	<input type="number" class="ftgNumberQuantity" onchange="quantityAnswersChange(this)" value="1"/>
	<h6>Enter correct answers:</h6>
	<div class="answers">
		<input type="text" class="ftgAnswer1"/>
	</div>
	<h6>Enter Max Point:</h6>
	<input type="number" id="MaxPoint" class = "maxpoint"/>
</div>

<!-- <body> -->




<div>
	
	<fieldset>
	<div style="width: 100%">
    		<img style="float:right;" class = "animated bounce" src="./img/giphy.gif">
	<h1>Quiz</h1>
		<p>Enter Quiz name  <input class="form-control" type="text" id="quizName" style="width: 200px;"></p>
		<p>Enter Quiz description <textarea class="form-control" id="quizDescription" style="width: 200px;"></textarea></p>
		<p><input type="checkbox" id="answersImmediately"/>answersImmediately</p>
		<p><input type="checkbox" id="isOnePage"/>isOnePage</p>
		<p> allowedTimeInMinutes: <input type="number" id="allowedTimeInMinutes"/> </p>
		</div>
	</fieldset>
</div>
	<fieldset id="questions">
		<legend>Questions</legend>
		
		<button class="btn btn-primary" id="addQuestion" onclick="addQuestion()">Add Question</button>
	</fieldset>
	<button class = "but" id="addQuiz">Add Quize</button>
 </body>  
<script>
var bla;
$('#addQuiz').click(function(){
	var quiz = {};
	quiz.name = $("#quizName").val();
	quiz.description = $("#quizDescription").val();
	quiz.answersImmediately = $ ("answersImmediately").is(":checked");
	quiz.random = $("#quizRandom").is(":checked");
	quiz.isOnePage = $("#quizIsOnePage").is(":checked");
	quiz.feedback = $("#quizFeedback").is(":checked");
	quiz.allowedTimeInMinutes = $("#allowedTimeInMinutes").val();
	quiz.questions = [];
	bla = quiz;
	if(quiz.name == "" || quiz.description == ""){
		alert("You should fill all fields of quiz");
	}else{
		var questions = $("#questions").find(".question");
		for(var i=0; i<questions.length; i++){
			var question = questions[i];
			switch($(question).find(".questionTypeSelect").val()){
				case "questionAnswerTemplate":{
					var obj = {category: 1};
					obj.question = $(question).find(".questionText").val();
					obj.answer = $(question).find(".questionAnswer").val();
					obj.maxpoint = $(question).find(".maxpoint").val();
					if(obj.question == "" || obj.answer == "" || obj.maxpoint==""){
						alert("fill all fields of question "+(i+1));
						return;
					}
					quiz.questions.push(obj);
					break;
				}
				case "multipleChoiceTemplate":{
					var obj = {category: 2}
					obj.question = $(question).find(".questionText").val();
					for(var j=1; j<=4; j++)
						obj["choice"+j] = $(question).find(".choice"+j).val();
					obj.answer = $(question).find(".correctAnswer").val();
					console.log(obj);
					obj.maxpoint = $(question).find(".maxpoint").val();
					if(obj.question == "" || obj.choice1 == "" || obj.choice2 == "" || obj.choice3 == "" || obj.choice4 == "" || 
							obj.answer == ""){
						alert("fill all fields of question "+(i+1));
						return;
					}
					quiz.questions.push(obj);
					break;
				}
				case "pictureQuestionTemplate":{
					var obj = {category :3 }
					obj.url = $(question).find(".pictureURL").val();
					obj.question = $(question).find(".questionText").val();
					obj.answer = $(question).find(".pictureAnswer").val();
					obj.maxpoint = $(question).find(".maxpoint").val();
					if(obj.url == "" || obj.answer == ""){
						alert("fill all fields of question "+(i+1));
						return;
					}
					quiz.questions.push(obj);
					break;
				}
				case "fillTheGapsTemplate":{
					var obj = {category :4 }
					obj.question=$(question).find(".questionText").val();
					obj.maxpoint = $(question).find(".maxpoint").val();
					obj.answers = [];
					for(var k=1; k<=$(question).find(".ftgNumberQuantity").val(); k++){
						obj.answers.push($(question).find(".ftgAnswer"+k).val());
					}
					console.log(obj);
					if(obj.question == ""){
						alert("fill all fields of question "+(i+1));
						return;
					}
					for(var k=1; k<=$("ftgNumberQuantity").val(); k++){
						if(obj["answer"+k] == ""){
							alert("fill all fields of question "+(i+1));
							return;
						}
					}
					quiz.questions.push(obj);
					break;
				}
			}
		}
		console.log(quiz);
		$.ajax({
			url: "addQuizServlet",
			type: "post",
			data: JSON.stringify(quiz),
			success: function(data){
				window.location.replace(data);
			},
			error: function(err){
				alert("err");
			} 
		});
	}
});
function removeQuestion(e){
	$(e).parent().parent().remove();
}
function changePictureURL(e){
	$(e).parent().find(".pqImage").attr('src', $(e).val());
}
function quantityAnswersChange(e){
	var answers = $(e).parent().find(".answers");
	var html = "";
	for(var i=1; i<=$(e).val(); i++){
		html += '<input type="text" class="ftgAnswer'+i+'"/>';
	}
	answers.html(html);
}
function addQuestion(){
	var elem = $(document.getElementById("questionTemplate").innerHTML);
	$(elem).find(".questionTypeSelect").change(function(e){
		$(this).parent().next().html($("#"+$(this).val()).html());
	});
	$(elem).insertBefore('#addQuestion');
	window.scrollTo(0,document.body.scrollHeight);
}
</script>
<%} %>
</html>