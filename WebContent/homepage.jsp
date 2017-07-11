<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello <% out.print(request.getSession().getAttribute("username")); %></title>
<style>
 #cover{
 display:table;
 }
 #nrmbt{
  display:table-cell;
 }
 
</style>
</head>
<body>

				
<div id = "cover">
 <input id = "nrmbt" type="button" onclick="location.href='createQuiz.html'" value="All about quizes" />
 <input id = "nrmbt" type="button" onclick="location.href='friends.html'" value = "Friends">
 <input id = "nrmbt" type="button" value = "Announcements" />  	
</div>		
 <form method="post" action="RegistrationServlet" type="text/css" style="margin-top:80px; padding-left:300px;">
 		  <p>Welcome <% out.print(request.getSession().getAttribute("username")); %> !</p>
 		  
     </form>
	

</body>
</html>