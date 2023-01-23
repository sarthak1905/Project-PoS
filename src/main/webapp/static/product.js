
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			showBrandDropdownAdd(data);
			showBrandDropdownEdit(data);
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
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

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var p = data[i];
		var buttonHtml = '<button class="btn btn-primary" onclick="deleteProduct(' + p.id + ')">Delete</button>'
		buttonHtml += ' <button class="btn btn-primary" onclick="displayEditProduct(' + p.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + p.id + '</td>'
		+ '<td>'  + p.barcode + '</td>'
		+ '<td>' + p.brand + '</td>'
		+ '<td>'  + p.category + '</td>'
		+ '<td>'  + p.name + '</td>'
		+ '<td>'  + p.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function showBrandDropdownAdd(data){
	const brandNames = new Set();
	const categoryNames = new Set();
	var $selectCategoryName = $("#inputProductBrandCategoryName");
	$selectCategoryName.empty();

	var $selectBrandName = $("#inputProductBrandName");
	$selectBrandName.empty();

	for(var i in data){
		var brandDetails = data[i];
		brandNames.add(brandDetails.brand);
		categoryNames.add(brandDetails.category);

	}

	for(category of categoryNames.values()){
		var option1 = $('<option></option>').attr("value", category).text(category);
        $selectCategoryName.append(option1);
	}

	for(brand of brandNames.values()){
		var option2 = $('<option></option>').attr("value", brand).text(brand);
		$selectBrandName.append(option2);
	}
}

function showBrandDropdownEdit(data){
	const brandNames = new Set();
	const categoryNames = new Set();
	var $selectCategoryName = $("#updateProductBrandCategoryName");
	$selectCategoryName.empty();

	var $selectBrandName = $("#updateProductBrandName");
	$selectBrandName.empty();

	for(var i in data){
		var brandDetails = data[i];
		brandNames.add(brandDetails.brand);
		categoryNames.add(brandDetails.category);
	}

	for(category of categoryNames.values()){
		var option1 = $('<option></option>').attr("value", category).text(category);
        $selectCategoryName.append(option1);
	}

	for(brand of brandNames.values()){
		var option2 = $('<option></option>').attr("value", brand).text(brand);
		$selectBrandName.append(option2);
	}
}


function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
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
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);
