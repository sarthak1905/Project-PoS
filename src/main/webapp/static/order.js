
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//BUTTON ACTIONS
function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();
	var url = getOrderUrl() + "/" + id;

	//Set the values to update
	var $form = $("#order-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();   
	   },
	   error: handleAjaxError
	});

	return false;
}

function addOrderItem(){
	var $tbody = $('#add-order-table').find('tbody');
	var formRow = createOrderItemForm();
	var row = '<tr>'+ formRow +'</tr>';
    $tbody.append(row);
}


function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteOrder(id){
	var url = getOrderUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getOrderUrl();

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

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var o = data[i];
		var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditOrder(' + o.id + ')">Edit</button>';
		var parsedDate = parseDate(o.dateTime);
		var row = '<tr>'
		+ '<td>' + o.id + '</td>'
		+ '<td>' + parsedDate + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function parseDate(dateTime){
	var hours = dateTime[3];
	var minutes = dateTime[4];
	var seconds = dateTime[5];
	var day = dateTime[2];
	var month = dateTime[1];
	var year = dateTime[0];
	var parsedDate = hours + ':' + minutes + ':' + seconds + ' ' + day;
	if(day == 1)
		parsedDate += 'st ';
	else if(day == 2)
		parsedDate += 'nd ';
	else if(day == 3)
		parsedDate += 'rd ';
	else 
		parsedDate += 'th ';
	
	if(month == 1)
		parsedDate += 'January';
	else if(month == 2)
		parsedDate += 'February';
	else if(month == 3)
		parsedDate += 'March';
	else if(month == 4)
		parsedDate += 'April';
	else if(month == 5)
		parsedDate += 'May';
	else if(month == 6)
		parsedDate += 'June';
	else if(month == 7)
		parsedDate += 'July';
	else if(month == 8)
		parsedDate += 'August';
	else if(month == 9)
		parsedDate += 'September';
	else if(month == 10)
		parsedDate += 'October';
	else if(month == 11)
		parsedDate += 'November';
	else if(month == 12)
		parsedDate += 'December';

	parsedDate += ', ' + year;
	return parsedDate;
}

function displayEditOrder(id){
	var url = getOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
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
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}

function displayOrder(data){
	$("#order-edit-form input[name=id]").val(data.id);	
	$("#order-edit-form input[name=created_at]").val(parseDate(data.dateTime));	
	$('#edit-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order-item').click(addOrderItem);
	$('#update-order').click(updateOrder);
	$('#refresh-data').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderList);
