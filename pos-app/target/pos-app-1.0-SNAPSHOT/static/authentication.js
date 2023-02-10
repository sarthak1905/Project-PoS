
function getSignupUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/site/signup";
}

function getLoginUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/site/login";
}

function submitSignupRequest(){
	var $form = $('#signup-form');
	var password = $('#signup-password').val();
	var email = $('#signup-email').val();
	var $emailErrorField = $('#signup-email-error');
	var $passwordErrorField = $('#signup-password-error');
	$emailErrorField.empty();
	$passwordErrorField.empty();

	if(isValidEmail(email) && isValidPassword(password)){
		var json = toJson($form);
		console.log(json);
		var url = getSignupUrl();

		$.ajax({
			url: url,
			type: 'POST',
			data: json,
			headers: {
				'Content-Type': 'application/json'
			},	   
			success: function(response){
				var loginUrl = getLoginUrl();
				window.location.replace(loginUrl);
			},
			error: handleAjaxError
			});
		}

	if(!isValidPassword(password)){
		var $errorMessage = '<small>Password must have at least 1 uppercase,' 
		+ ' lowercase, special char, and number each</small>';
		$passwordErrorField.append($errorMessage);
	}

	if(!isValidEmail(email)){
		$emailErrorField.empty();
		var $errorMessage = '<small>Please enter a valid email</small>';
		$emailErrorField.append($errorMessage);
	}
}

function isValidPassword(password){
	var upperCase = /[A-Z]/g;
	var lowerCase = /[a-z]/g;
	var numbers = /[0-9]/g;
	var specialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/g;
	
	if (password.match(upperCase) && password.match(lowerCase) && password.match(numbers) && password.match(specialChar)) {
		return true;
	} 
	return false;
}

function isValidEmail(email) {
	var regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return regex.test(email);
}

function togglePassword() {
	var $passwordInput =  $('#signup-password');
	if ($passwordInput.type === "password") {
	  $passwordInput.type = "text";
	} else {
	  $passwordInput.type = "password";
	}
  }

function init(){
	$('#signup-btn').click(submitSignupRequest);
	$('#show-password-checkbox').click(togglePassword);
}

$(document).ready(init);
