
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
	var password = $('#password').val();
	var email = $('#signup-email').val();

	if(isValidEmail(email) && isValidPassword(password)){
		console.log(password);
		console.log(email);
		console.log($form);
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
		var errorMessage = 'Password must have at least 1 uppercase,' 
		+ ' lowercase, special char, and number each';
		showErrorMessage(errorMessage);
	}

	if(!isValidEmail(email)){
		var errorMessage = 'Please enter a valid email';
		showErrorMessage(errorMessage);
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
	$passwordInput =  $('#password')[0];
	if ($passwordInput.type === "password") {
	  $passwordInput.type = "text";
	} else {
	  $passwordInput.type = "password";
	}
  }

function checkInfoMessageAfterSubmit(){
	wait(500);
	checkInfoMessage();
}

function checkInfoMessage(){
	if(location.href === 'http://localhost:9000' + getLoginUrl()){
		var message = $('#info-message').text().trim();
		console.log(message);
		console.log('reaching here');
		if(message === 'Signed up successfully!' || message == 'Logged out successfully'){
			showSuccessMessage(message);
		}
		else if (message === 'Invalid username or password'){
			showErrorMessage(message);
		}
	}
}

function init(){
	$('#signup-btn').click(submitSignupRequest);
	$('#show-password-button').click(togglePassword);
}

$(document).ready(init);
$(document).ready(checkInfoMessage);
