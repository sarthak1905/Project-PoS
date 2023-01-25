
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

//BUTTON ACTIONS
function addOrder(event){
	//Set the values to update
	var $form = $("#add-order-form");
	var jsonList = convertOrderForm($form);
	var url = getOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(jsonList),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();
			   $('#add-order-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function convertOrderForm($form){
	var serializedData = $form.serializeArray();
	var listLength = serializedData.length/3;
	var jsonList = [];
	for(let i=0; i<listLength; i++){
		var jsonData = {};
		for(let j=i*3; j<(i*3) + 3; j++){
			jsonData[serializedData[j]['name']] = serializedData[j]['value'];
		}
		jsonList.push(jsonData);
	}
	return jsonList;
}

function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#edit-order-id").val();
	var url = getOrderUrl() + "/" + id;

	//Set the values to update
	var $form = $("#edit-order-form");
	var jsonList = convertOrderForm($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(jsonList),
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

function removeOrderItem(){
	var tableId = $(this).closest('table').attr('id');
	var rowCount = $('#' + tableId + ' tr').length - 1;
	if(rowCount == 1){
		alert("Minimum 1 order item required!");
		return;
	}
	$(this).closest('tr').remove();
}

function addOrderItemRow() {
    $('#add-order-row').clone().insertAfter('tr.add-order-row:last');
	var $lastRowDropdown = $('tr.add-order-row:last td div select');
	getProductList($lastRowDropdown, add=true);
    $('tr.add-order-row:last input[name=quantity]').val('');
    $('tr.add-order-row:last input[name=sellingPrice]').val('');
    $('tr.add-order-row:last button').click(removeOrderItem);
}

function editAddOrderItemRow() {
    $('#edit-order-row').clone().insertAfter('tr.edit-order-row:last');
	$('tr.edit-order-row:last input[name=barcode]').replaceWith('<select class="form-control col-12" name="barcode">'
	+ '<option>Dummy</option>' 
	+'</select>');
	var $lastRowDropdown = $('tr.edit-order-row:last td div select');
	getProductList($lastRowDropdown, add=false);
	$('tr.edit-order-row:last input[name=quantity]').val('');
    $('tr.edit-order-row:last input[name=sellingPrice]').val('');
	$('tr.edit-order-row:last button').replaceWith('<button type="button" class="btn btn-danger">Remove</button>');
    $('tr.edit-order-row:last button').click(removeOrderItem);
}

function getProductList(element, add){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			if(add){
				showBarcodeDropdownAdd(data, element);
			}
			else{
				showBarcodeDropdownEdit(data, element);
			}
	   },
	   error: handleAjaxError
	});
}

function showBarcodeDropdownAdd(data, element){
	const barcodes = new Set();
	var $selectBarcodeInput = element;
	$selectBarcodeInput.empty();

	for(var i in data){
		var productDetails = data[i];
		barcodes.add(productDetails.barcode);
	}

	for(barcode of barcodes.values()){
		var option = $('<option></option>').attr("value", barcode).text(barcode);
        $selectBarcodeInput.append(option);
	}
}

function showBarcodeDropdownEdit(productData, element){
	var $selectBarcodeInput = element;
	$selectBarcodeInput.empty();

	getUniqueBarcodes(productData, element);
}

function getUniqueBarcodes(productData, $selectBarcodeInput){
	var id = $('#edit-order-id').val();
	var url = getOrderUrl() + '/' + id + '/items';
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			const barcodes = new Set();
			var map = new Map();
			for(var i in data){
				var orderItemDetails = data[i];
				map.set(orderItemDetails.barcode, 1);
			}
			for(var i in productData){
				var productDetials = productData[i];
				if(map.get(productDetials.barcode) == undefined){
					barcodes.add(productDetials.barcode);
				}
			}
			if(barcodes != undefined){
				for(barcode of barcodes.values()){
					var option = $('<option></option>').attr("value", barcode).text(barcode);
					$selectBarcodeInput.append(option);
				}
			}
		},
		error: handleAjaxError
	 });
}

function initOrderItemRow(){
	var $selectField = $('#add-order-table').find('tbody tr:first td:first div select');
	getProductList($selectField);
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
	if(day%10 == 1)
		parsedDate += 'st ';
	else if(day%10 == 2)
		parsedDate += 'nd ';
	else if(day%10 == 3)
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
	var url = getOrderUrl() + '/' + id + '/items';
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data, id);   
	   },
	   error: handleAjaxError
	});	
}

function displayOrder(data, id){
	$('#edit-order-id').val(id);
	var $editTbody = $("#edit-order-tbody");
	for(let i = 0; i<data.length; i++){
		var $row;
		if(i == 0){
			$row = $editTbody.find('tr.edit-order-row:first');
			$editTbody.empty();
			$editTbody.append($row);
		}
		else{
			$editTbody.find('tr.edit-order-row:first').clone().insertAfter('tr.edit-order-row:last');
			$row = $editTbody.find('tr:last');
		}
		$row.find('td div input[name=barcode]').val(data[i].barcode);
		$row.find('td div input[name=quantity]').val(data[i].quantity);
		$row.find('td div input[name=sellingPrice]').val(data[i].sellingPrice);
	}
	$('#edit-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order-item').click(addOrderItemRow);
	$('#edit-add-order-item').click(editAddOrderItemRow);
	$('#add-order-dialog').click(initOrderItemRow);
	$('#add-order').click(addOrder);
	$('#add-order-first-row-btn').click(removeOrderItem);
	$('#update-order').click(updateOrder);
	$('#refresh-data').click(getOrderList);
}

$(document).ready(init);
$(document).ready(getOrderList);
