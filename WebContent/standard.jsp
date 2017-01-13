<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Netezza Request</title>
</head>
<body>
	<form class="form1" action="upload" method="post" enctype="multipart/form-data">
		<p>Start the validation process</p>
		<br/>
		<p>Select refresh template</p>
		<br/>
		<input id="metadataFile" type="file" name="metadata" />
		<input type="hidden" name="from" value="internetexplorer" />
		<input id="submit" class="orangebutton" type="submit" value="Validate" />
	</form>
</body>
</html>
