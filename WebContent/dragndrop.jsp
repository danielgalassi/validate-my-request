<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Netezza Refresh</title>
<!--script>
	window.onload = function() {
		var dropbox = document.getElementById("dropbox");
		dropbox.addEventListener("dragenter", noop, false);
		dropbox.addEventListener("dragexit", noop, false);
		dropbox.addEventListener("dragover", noop, false);
		dropbox.addEventListener("drop", dropUpload, false);
	}

	function noop(event) {
		event.stopPropagation();
		event.preventDefault();
	}

	function dropUpload(event) {
		noop(event);
		var files = event.dataTransfer.files;
		upload(files[0]);
	}

	function upload(file) {
		var formData = new FormData();
		formData.append("file", file);
//		var xhr = new XMLHttpRequest();
//		xhr.open("POST", "ValidatorService", false);
//		xhr.send(formData);
	}
</script-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style.css" type="text/css" />
</head>
<body>
	<h1 style="color: #e68a00" align="center">Validate-My-Request</h1>
	<div class="wrapper" align="center">
		<form class="form1" method="post" action="ValidatorService"
			enctype="multipart/form-data">
			<div class="formtitle">Start the validation process</div>
			<div class="input nobottomborder">
				<div class="upload-drop-zone" id="metadata">Just drag and drop
					files here</div>
			</div>
			<div class="buttons">
				<input class="orangebutton" type="submit" value="Upload" />
			</div>
		</form>
	</div>

</body>
</html>
