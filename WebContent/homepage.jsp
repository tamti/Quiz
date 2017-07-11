<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello <% out.print(request.getSession().getAttribute("username")); %></title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<style>
body{
   background-image: url("topography.png");
}
 
</style>
</head>
<body>

				
<div align="center" id = "cover">
 <input  class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='createQuiz.html'" value="All about quizes" />
 <input class="btn btn-warning hell" style="width:25%" type="button" onclick="location.href='friends.html'" value = "Friends">
 <input  class="btn btn-warning hell" style="width:25%" type="button" value = "Announcements" />
 <input class="btn btn-warning hell" style="width:25%" type="button" value = "Sign out" />
</div>		
 <form align="center" method="post" action="RegistrationServlet" type="text/css" style="margin-top:10%; ">
 		  <p>Welcome <% out.print(request.getSession().getAttribute("username")); %> !</p>
 		  
     </form>
	

</body>
</html>