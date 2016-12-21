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
				<div style="line-height: 150%;" class="inputtext" align="center">Select refresh template
				<div class="input nobottomborder">
				<input id="metadataFile" type="file" name="metadata" style="padding: 5%; border: 1px solid #ddd;"/>
				</div>
				</div>
			</div>
			<div class="buttons">
				<input id="submit" class="orangebutton" type="submit"
					value="Validate" />
			</div>
			<input type="hidden" name="from"
				value="${pageContext.request.requestURI}" />
		</form>
	</div>
</body>
</html>
