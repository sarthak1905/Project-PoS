
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function getRole(){
    return $('#user-role').text().trim();
}

function getInfoMessage(){
    return $('#info-message').text().trim();
}

function displayOrHideButtons(){
	var role = getRole();
	if(role === 'operator'){
		$('.btn-add').attr('disabled', true);
        $('#add-order-dialog').removeAttr('disabled');
		$('.btn-upload').attr('disabled', true);
        $('.input-form :input').prop('disabled', true);
        $('#refresh-data').removeAttr('disabled');
	}
}

function handleAjaxError(response){
	var message = JSON.parse(response.responseText)['message'].replace('[', '').replace(']','');
    showErrorMessage(message);
}

function showErrorMessage(message){
    Toastify({
        text: message,
        duration: 5000, 
        close: true,
        stopOnFocus: true,
        style: {
          background: "linear-gradient(to right, #EC3F2C, #FF5733)",
        },
        }).showToast();
    throw new Error(message);
}

function showSuccessMessage(message){
    Toastify({
        text: message,
        duration: 5000,
        close: true,
        stopOnFocus: true, 
        style: {
          background: "linear-gradient(to right, #349E10, #40E408)",
        },
    }).showToast();
}

function checkRoleAndDisableEditBtns(){
    var role = getRole();
	if(role === 'operator'){
		disableEditBtns();
	}
}

function disableEditBtns(){
    $('button.btn-edit').attr('disabled', true);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

$(document).ready(displayOrHideButtons);
