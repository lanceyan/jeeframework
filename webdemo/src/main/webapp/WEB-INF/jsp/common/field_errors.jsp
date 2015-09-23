<%-- ActionError Messages - usually set in Actions --%>
<%@ page contentType="text/html;charset=GBK"%>


<script>
var iconWarning = "./images/iconWarning.gif";
var iconRight = "./images/iconRight.gif";

var fieldErrors = new Array();
<s:if test="hasFieldErrors()">
      <s:iterator value="fieldErrors">
          fieldErrors[fieldErrors.length] = ["<s:property value="key" />","<s:property value="value" />"];
      </s:iterator>
</s:if>

function clearErrorMessages(fieldId) {
	var spanField = document.getElementById("errorFor-" + fieldId);
	if (spanField != null) {
	    spanField.innerHTML = "<img src='" + iconRight + "'>ÌîÐ´ÕýÈ·";
	}
}

function addError(ee, errorText) {
	try {
		var errorForField = document.getElementById('errorFor-' + ee.id);
		if (errorForField != null) errorForField.innerHTML = "<img src='" + iconWarning + "'><font color='red'>" + errorText + "</font>";
	} catch (e) {
	alert(e);
	}
}

function initFieldError() {
	for (var i = 0; i < fieldErrors.length; i++) {
		var fieldName = fieldErrors[i][0];
		var fieldErrorMessage = fieldErrors[i][1];
		
		fieldErrorMessage = fieldErrorMessage.replace("[","");
		fieldErrorMessage = fieldErrorMessage.replace("]","");
		
		var fields = document.getElementsByName(fieldName);
		
		if (fields != null && fields.length == 1) {
			addError(fields[0],fieldErrorMessage);
		}
		else
		{
		    var field = document.getElementById(fieldName);
			if (field != null && field.length == 1) {
				addError(field ,fieldErrorMessage);
			}
		}
	}
}

</script>


