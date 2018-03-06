<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
<title>Register User</title>
</head>
<body>

	<nav>
    <div class="nav-wrapper">
      <a href="#" class="brand-logo" style="margin-left: 15px">VPTGame</a>
    </div>
  </nav>
	
	<div class="row">
	
		<form class="card col s12 m6 l6" action="Insert" method="POST" style="margin-left: 20%; margin-top: 35px;">
		
			<div class="input-field">
	          <input name="username" id="username" type="text" class="validate">
	          <label for="username">Username</label>
	        </div>
	        
	        <div class="input-field">
	          <input name="password" id="password" type="password" class="validate">
	          <label for="password">Password</label>
	        </div>
	        
	        <div class="input-field">
			    <select name="country">
			      <option value="" disabled selected>Choose your option</option>
			      <option value="1">India</option>
			      <option value="2">U.S.A</option>
			      <option value="3">Spain</option>
			    </select>
			    <label>Country</label>
			 </div>
	        
	        <div class="input-field">
	          <input name="submit" id="submit" type="submit" class="waves-effect waves-light btn" value="Register" style="padding-top: 5px; margin: 15px">
	        </div>
			
		</form>
	
	</div>


	<script type="text/javascript">
		$(document).ready(function() {
		    $('select').material_select();
		});
	</script>
</body>
</html>