<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Netezza Request</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style.css" type="text/css" />
</head>
<body>
	<div class="wrapper">
		<form class="form1" action="upload" method="post"
			enctype="multipart/form-data">
			<div class="formtitle">Start the validation process</div>

			<div class="input nobottomborder2">	
				<div class="inputtext">Select refresh template</div>
				<div class="inputcontent nobottomborder">
					<input align="right" id="metadataFile" type="file" name="metadata" size="15" />
				</div>
			</div>
			<div class="buttons">
				<input id="submit" class="orangebutton" type="submit" style="opacity: 0.25;"
					value="Validate" />
			</div>
			<input type="hidden" name="from" value="${pageContext.request.requestURI}"/>
		</form>
	</div>
</body>
</html>
