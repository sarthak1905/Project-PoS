
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		refreshTable();
			$('#add-brand-modal').modal('toggle');
			message = 'Brand added successfully!';
			showSuccessMessage(message);  
	   },
	   error: function(response){ 
		handleAjaxError(response);
	   }
	});

	return false;
}

function updateBrand(event){
	
	//Get the ID
	var id = $("#brand-edit-form input[name=brandId]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			$('#edit-brand-modal').modal('toggle');
	   		refreshTable();  
			message = 'Brand updated successfully!';
			showSuccessMessage(message);
	   },
	   error: function(response){
		handleAjaxError(response);
	   }
	});

}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
			dataTablize();
	   },
	   error: function(response){
		handleAjaxError(response);
	   }
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
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
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		var buttonHtml = '<button class="btn btn-edit button" onclick="displayEditBrand(' + b.id + 
		')"><i class="bi bi-pen-fill"></i> Edit</button>';
		var row = '<tr>'
		+ '<td>' + b.brand + '</td>'
		+ '<td>'  + b.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	checkRoleAndDisableEditBtns();
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);   
	   },
	   error: function(response){
		handleAjaxError(response);
	   } 
	});	
}

function resetUploadDialog(){
	$('#download-errors').attr('disabled', true);
	$('#process-data').attr('disabled', true);
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var $file = $('#brandFile');
	var fileName = document.getElementById("brandFile").files[0].name;
	if(fileName.slice(-4) != '.tsv'){
		showErrorMessage('Please upload .tsv file only!');
		return;
	}
	$('#brandFileName').html(fileName);
	$('#process-data').attr('disabled', false);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}

function refreshTable(){
	destroyTablize();
	getBrandList();
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=brandId]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(refreshTable);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);
