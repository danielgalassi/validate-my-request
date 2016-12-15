<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Netezza Request - ERROR</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style.css" type="text/css" />
</head>
<body>

	<div class="wrapper" style="width: 25%;">
		<form class="form1" action="/validate-my-request/">
			<div class="formtitle">ERROR</div>
			<div class="input nobottomborder2">
				<c:if test="${not empty ErrorMessage}">
					<div class="inputtext2">${ErrorMessage}</div>
				</c:if>
				<c:if test="${emptyErrorMessage}">
					<div class="inputtext2">${ErrorMessage}</div>
				</c:if>
				<c:if test="<%=response.getStatus() >= 400%>">
					<div class="inputtext2">Sorry mate!</div>
					<div class="inputtext2">
						HTTP
						<%=response.getStatus()%>
						error. Something is not right...
					</div>
				</c:if>
			</div>
			<div class="buttons">
				<input class="orangebutton" type="submit"
					value="Back" />
			</div>
		</form>
	</div>

</body>
</html>
