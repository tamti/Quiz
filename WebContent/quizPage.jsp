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
System.out.println("username: "+username);
AccountManager accountman = new AccountManager();
User user = accountman.getUser(username);
if(user == null){
	response.sendRedirect("homepage.jsp");
}else{
	String quizName = request.getParameter("quizname");
	System.out.println("quizname: "+quizName);
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

				<form action="searchServlet" method = "POST" id = "search">
					<input id="searchForm" type = "text" name="q" size="60" placeholder="Search ...">
				</form>
				<a href="https://instagram.com/"><img class = "link" src="./img/2.png"></a>
				<p id = "bla" class="out"><a href="SignOutServlet">Sign Out</a></p>
				<p id = "bla" class = "boloshi"><a href="homepage.jsp"><%=user.getFirstName() %> <%=user.getLastName() %></a></p>
				<a href="homepage.jsp"><img class = "user" src="./img/user.png"></a>
				
			</div>
		</div>
		<div style="width: 100%">
    		<img style="float:right;" class = "animated bounce" src="./img/minuka.jpg">
		<h2>Name of Quiz: <%=quiz.getQuizName()%></h2>
		<h3>Description: <%=quiz.getDescription() %></h3>
		<h4>Creator: <%=accountman.getUserById(quiz.getOwnerID())%></h4>
		<h4>number of questions: <%=quiz.getQuestions().size()%></h4>
		
		<%ArrayList <Question> questions = quiz.getQuestions();
		
		if(!questions.isEmpty()){
		  for(int i = 0;i< questions.size();i++){ %>
		     <div class="question">
			 <p> <%=(questions.get(i).getQuestionStr())%> </p> 
			 <% System.out.println(quiz.isOnePage());
			 QuestionType currType = questions.get(i).getType();
		     if(currType.equals(QuestionType.basicResponse)){ %>
		        <input type="hidden" value="1" class="type">
		        <textarea rows="2" cols="15" class="questionAnswer"></textarea>
		    	 <% 
		    	 
		     }else if(currType.equals(QuestionType.fillInTheBlank)){
		    	 %><input type="hidden" value="4" class="type">
		    	 <input type="hidden" class="numAnswers" value= <%=questions.get(i).getAnswers().size()%> > <%
		    	 for (int j = 0; j< questions.get(i).getAnswers().size();j++){
		    		 %>
		    		 <textarea rows="2" cols="15" class="answ<%=j %>"></textarea>
		    		 <%  
		    	 }
		    	
		     }else if(currType.equals(QuestionType.multipleChoice)){
		    	 %>     <input type="hidden" value="2" class="type">
		    	 		<div class="questionAnswer" >
							<input class="answer" type="checkbox" value="A" checked="checked"><span><%=questions.get(i).getAnswers().get(0).getAnswerStr()%></span>
							<br>
							<input class="answer" type="checkbox" value="B"><span><%=questions.get(i).getAnswers().get(1).getAnswerStr()%></span>
							<br>
							<input class="answer" type="checkbox" value="C"><span><%=questions.get(i).getAnswers().get(2).getAnswerStr()%></span>
							<br>
							<input class="answer" type="checkbox" value="D"><span><%=questions.get(i).getAnswers().get(3).getAnswerStr()%></span>
						</div>
		    	 <%  
		     }else if(currType.equals(QuestionType.pictureResponse)){
		    	 System.out.println(questions.get(i).getPhoto());
		    	 %> 
		    	    <input type="hidden" value="3" class="type">
		    	    <img src="<%=questions.get(i).getPhoto()%>" width="100px" height="100px"></img>
					<textarea rows="2" cols="15" class="questionAnswer"></textarea>
		    	 <% 
		     }
				 %>			
					 
			<% }%>
		 
		</div>
		<%}  }%>
		 <input type = "submit" value = "submit" onclick="submitQuiz(this)">
		 </div>
		<script>
		
		$('input[type="checkbox"]').on('change', function() {
			   $(this).siblings('input[type="checkbox"]').prop('checked', false);
			});
    
	function submitQuiz(e){
		var questions = $(".question");
		var answers = [];
		var been = false;
		var counter = 0;
		var multiples = [];
		for(var i=0; i< questions.length; i++){
			var question = questions[i];
			switch($(question).find(".type").val()){
			case "1":{
				if($(question).find(".questionAnswer").val() === ""){
					alert("Please don't leave little gap empty!");
				}
				answers.push($(question).find(".questionAnswer").val());
				break;
			}
			case "2":{
	            
				//alert($(question).find(".questionAnswer :checked").next('span').text());
				//need to add separatelly all the strings
				if(!been){
					
					//alert($('.answer:checked').next('span').text());
				/* 	var checkedValue = null; 
					var inputElements = document.getElementsByClassName('answer');
					for(var j=0; inputElements[j]; ++j){
					      if(inputElements[j].checked){
					    	   // alert(inputElements[j].value);
					    	   multiples.push($(question).find(".questionAnswer :checked").next('span').text());
					           checkedValue = inputElements[j].value;
					      }
					} */
					been = true;
				}
				//alert(multiples[counter]);
	            counter ++;
		
				//answers.push($(question).find(".questionAnswer :checked").next('label').text());
				break;
			}
			case "3":{
				if($(question).find(".questionAnswer").val() === ""){
					alert("Please don't leave little gap empty!");
				}
				answers.push($(question).find(".questionAnswer").val());
				break;
			}
			case "4":{
				var num = $(question).find(".numAnswers").val();
				var arr = [];
				for(var j=0; j<num; j++){
					if(($(question).find(".answ"+j).val()) === ""){
						alert("Please don't leave little gap empty!");	
					}
					answers.push($(question).find(".answ"+j).val());
				}
				break;
			}
		}
			
		}
		
		$.ajax({
			url: "submitQuizServlet",
			type: "post",
			data: JSON.stringify(answers),
			headers: {
				'content-type': 'application/json'
			},
			success: function(data){
				//bla = data;
				console.log(data);
				var d = JSON.parse(data);//???
				console.log(d);
				//alert(d.value);
				window.location.replace(d.url);//linkze gadasvla
			},
			error: function(e){
				alert("error");
			}
		});
	}		
		</script>
		</body>
</html>