//Get XMLHTTP Object
function getXMLHTTPObject() {
	var xmlhttpObject = null;
	try {
		// For Old Microsoft Browsers
		xmlhttpObject = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		try {
			// For Microsoft IE 6.0+
			xmlhttpObject = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e1) {
			// No Browser accepts the XMLHTTP Object then false
			xmlhttpObject = false;
		}
	}
	if (!xmlhttpObject && typeof XMLHttpRequest != 'undefined') {
		// For Mozilla, Opera Browsers
		xmlhttpObject = new XMLHttpRequest();
	}
	// Mandatory Statement returning the ajax object created
	return xmlhttpObject;
}

// Change the value of the outputText field
function setAjaxOutput() {
	document.getElementById('ajaxResponse').innerHTML = xmlhttpObject.responseText;
}

function handleServerResponse() {
	if (xmlhttpObject.readyState == 4) {
		if (xmlhttpObject.status == 200) {
			setAjaxOutput();
		} else {
			alert("Error during AJAX call. Please try again");
		}
	}
}

// Implement business logic
function doAjaxCall() {
	xmlhttpObject = getXMLHTTPObject();
	if (xmlhttpObject != null) {
//		var URL = "AjaxAction?userName=" + document.getElementById('userName').value;
		var URL = "ValidatorService";
		xmlhttpObject.open("POST", URL, true);
		xmlhttpObject.send(null);
		xmlhttpObject.onreadystatechange = handleServerResponse;
	}
}