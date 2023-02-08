
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
	var response = JSON.parse(response.responseText)['message'].replace('[', '').replace(']','');
    Toastify({
        text: response,
        duration: 5000, 
        close: true
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
