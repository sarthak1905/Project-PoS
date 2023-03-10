
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	console.log(json);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();
			refreshTable();
			message = 'Inventory updated successfully!';
			showSuccessMessage(message);   
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
			dataTablize();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	resetUploadDialog();
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length > 5000){
		showErrorMessage('Row limit of 5000 exceeded!');
	}
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		if(errorData.length != 0){
			$('#download-errors').attr('disabled',false);
		}
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	var json = JSON.stringify(row);
	var url = getInventoryUrl() + "/file-upload/"+ row["barcode"];

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		var buttonHtml = ' <button class="btn btn-edit button" onclick="displayEditInventory(' + b.id + ')"><i class="bi bi-pen-fill"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>' + b.barcode + '</td>'
		+ '<td>' + b.name + '</td>'
		+ '<td>'  + b.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	checkRoleAndDisableEditBtns();
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	$('#download-errors').attr('disabled', true);
	$('#process-data').attr('disabled', true);
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");


	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = document.getElementById("inventoryFile").files[0].name;

	if(fileName.slice(-4) != '.tsv'){
		showErrorMessage('Please upload .tsv file only!');
		return;
	}

	$('#inventoryFileName').html(fileName);
	$('#process-data').attr('disabled', false);
}

function refreshTable(){
	destroyTablize();

}


function displayUploadData(){
 	resetUploadDialog();	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=name]").val(data.name);	
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-id").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getInventoryList);
