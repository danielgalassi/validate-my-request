<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Netezza Request</title>
<script type="text/javascript" src="js/ajax.js"></script>
<script>
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

	function noopexit(event) {
		event.stopPropagation();
		event.preventDefault();
	}

	function dropUpload(event) {
		noop(event);
		var files = event.dataTransfer.files;

		for (var i = 0; i < files.length; i++) {
			upload(files[i]);
		}
	}

	function upload(file) {
		var formData = new FormData();
		formData.append("file", file);

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "upload", false);
		xhr.send(formData);
//		document.getElementById("submit").disabled = false;
//		document.getElementById("submit").style = "opacity: 1.0";
		doAjaxCall();
	}
</script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style.css" type="text/css" />
</head>
<body>
	<div class="wrapper">
		<form class="form1" method="post" action="ValidatorService"
			enctype="multipart/form-data">
			<div class="formtitle">Start the validation process</div>
				<div class="upload-drop-zone" id="dropbox">Drag and drop your request here</div>
			<input type="hidden" name="from"
				value="dragndrop" />
			<!--div class="buttons">
				<input id="submit" class="orangebutton" type="submit" style="opacity: 0.25;"
					value="Validate" disabled="disabled" />
			</div-->
		</form>
	</div>

	<div id="ajaxResponse"></div>
</body>
</html>
