
function getProductUrl(){
	var baseUrl = $('meta[name=baseUrl]').attr('content')
	return baseUrl + '/api/products';
}

function getBrandUrl(){
	var baseUrl = $('meta[name=baseUrl]').attr('content')
	return baseUrl + '/api/brands';
}

// Global variables 
const brandCategoriesMap = {}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $('#product-form');
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
	   		refreshTable();
			message = 'Product added successfully!';
			showSuccessMessage(message);
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	console.log('reaching here');
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
	   		refreshTable();
			message = 'Product updated successfully!';
			showSuccessMessage(message);
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
			dataTablize();
	   },
	   error: handleAjaxError
	});
}

function generateBrandCategoriesMap(){
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(brandData) {
			for(var i in brandData){
				var tempBrand = brandData[i];
				if(tempBrand.brand in brandCategoriesMap){
					brandCategoriesMap[tempBrand.brand].push(tempBrand.category); 
				}
				else{
					brandCategoriesMap[tempBrand.brand] = [tempBrand.category];
				}
			}
			showBrandDropdown(Object.keys(brandCategoriesMap)[0], undefined, true, false);
		},
		error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processProductData(){
	var file = $('#productFile')[0].files[0];
	resetProductUploadDialog();
	readFileData(file, readProductFileDataCallback);
}

function readProductFileDataCallback(results){
	fileData = results.data;
	if(fileData.length > 5000){
		showErrorMessage('Row limit of 5000 exceeded!');
	}
	uploadProductRows();
}

function uploadProductRows(){
	//Update progress
	updateProductUploadDialog();
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
	var url = getProductUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadProductRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText;
	   		errorData.push(row);
	   		uploadProductRows();
	   }
	});

}

function downloadProductErrors(){
	writeFileData(errorData);
}

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var p = data[i];
		var buttonHtml = ' <button class="btn btn-edit button" onclick="displayEditProduct(' + p.id + ')"><i class="bi bi-pen-fill"></i> Edit</button>'
		var row = '<tr>'
		+ '<td>'  + p.barcode + '</td>'
		+ '<td>' + p.brand + '</td>'
		+ '<td>'  + p.category + '</td>'
		+ '<td>'  + p.name + '</td>'
		+ '<td>'  + p.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	checkRoleAndDisableEditBtns();
}

function showBrandDropdown(brand, category, firstRun, isInEditModal){
	const brandNames = new Set();
	const categoryNames = new Set();
	
	var selectCategoryString = 'ProductBrandCategoryName';
	var selectBrandString = 'ProductBrandName';
	if(isInEditModal){
		selectCategoryString = '#update' + selectCategoryString;
		selectBrandString = '#update' + selectBrandString;
	}
	else{
		selectCategoryString = '#input' + selectCategoryString;
		selectBrandString = '#input' + selectBrandString;
	}

	var $selectCategoryName = $(selectCategoryString);
	$selectCategoryName.empty();

	var $selectBrandName = $(selectBrandString);
	if(firstRun){
		$selectBrandName.empty();
	}

	for(const [key, value] of Object.entries(brandCategoriesMap)){
		if(firstRun){
			brandNames.add(key);
		}
		if(key === brand){
			for(const category of value){
				categoryNames.add(category);
			}
		}
	}

	for(categoryName of categoryNames.values()){
		var option1 = $('<option></option>').attr("value", categoryName).text(categoryName);
        $selectCategoryName.append(option1);
	}

	for(brandName of brandNames.values()){
		var option2 = $('<option></option>').attr("value", brandName).text(brandName);
		$selectBrandName.append(option2);
	}

	if(firstRun){
		$selectBrandName.select2();
		$selectCategoryName.select2();
	}

	if(isInEditModal){
		$selectBrandName.val(brand).trigger('change');
		$selectCategoryName.val(category).trigger('change');
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

function resetProductUploadDialog(){
	$('#download-errors').attr('disabled', true);
	$('#process-data').attr('disabled', true);
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateProductUploadDialog();
}

function updateProductUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateProductFileName(){
	var $file = $('#productFile');
	var fileName = document.getElementById("productFile").files[0].name;
	if(fileName.slice(-4) != '.tsv'){
		showErrorMessage('Please upload .tsv file only!');
		return;
	}
	$('#productFileName').html(fileName);
	$('#process-data').attr('disabled', false);
}

function displayProductUploadData(){
 	resetProductUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$('#product-edit-form input[name=name]').val(data.name);
	$('#product-edit-form input[name=brand]').val(data.brand);
	$('#product-edit-form input[name=category]').val(data.category);
	$('#product-edit-form input[name=barcode]').val(data.barcode);
	$('#product-edit-form input[name=mrp]').val(data.mrp);
	showBrandDropdown(data.brand, data.category, true, true);
	$('#edit-product-modal').modal('toggle');
}

function refreshTable(){
	destroyTablize();
	getProductList();
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(refreshTable);
	$('#upload-data').click(displayProductUploadData);
	$('#process-data').click(processProductData);
	$('#download-errors').click(downloadProductErrors);
    $('#productFile').on('change', updateProductFileName);
	$('#inputProductBrandName').on('select2:select', function(e){
		var data = e.params.data.text;
		showBrandDropdown(data, undefined, firstRun=false, isInEditModal=false);
	});
	$('#updateProductBrandName').on('select2:select', function(e){
		var data = e.params.data.text;
		showBrandDropdown(data, undefined, firstRun=false, isInEditModal=true);
	});
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(generateBrandCategoriesMap);
