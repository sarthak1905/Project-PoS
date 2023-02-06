
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
	var $lastRowDropdown = $('tr.add-order-row:last td div datalist');
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
	var $lastRowDropdown = $('tr.edit-order-row:last td div datalist');
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
		$selectBarcodeInput.append($('<option></option>').attr("value", barcode).text(barcode));
	}
	$selectBarcodeInput.select2();
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
					$selectBarcodeInput.append($('<option></option>').attr("value", barcode).text(barcode));
				}
			}
			$selectBarcodeInput.select2();
		},
		error: handleAjaxError
	 });
}

function initOrderItemRow(){
	var $selectField = $('#add-order-table').find('tbody tr:first td:first select');
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

function downloadOrderInvoice(id){
	var url = getOrderUrl();
	url += '/' + id + '/invoice';
	$('#btn-edit' + id).remove();
	window.location.href = url;
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var o = data[i];
		var downloadIncvoiceButton = ' <button id="btn-invoice' + o.id + '"class="btn btn-primary" onclick="downloadOrderInvoice(' + o.id + ')">Get Invoice</button>';
		var actionsButton = ' <button id="btn-view' + o.id + '"class="btn btn-primary" onclick="displayOrderItems(' + o.id + ')">View</button>';
		if(o.invoiced === false){
			actionsButton += ' <button id="btn-edit' + o.id + '"class="btn btn-primary" onclick="displayEditOrder(' + o.id + ')">Edit</button>';
		}
		var row = '<tr>'
		+ '<td>' + o.id + '</td>'
		+ '<td>' + o.dateTime + '</td>'
		+ '<td>' + o.orderTotal + '</td>'
		+ '<td>' + actionsButton + '</td>'
		+ '<td>' + downloadIncvoiceButton + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayOrderItems(id){
	var url = getOrderUrl() + '/' + id + '/items';
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		viewOrderItems(data, id);   
	   },
	   error: handleAjaxError
	});	
}

function viewOrderItems(data, id){
	$('#view-order-id').val(id);
	console.log(data);
	var $tbody = $('#view-order-tbody');
	$tbody.empty();
	for(var i in data){
		var item = data[i];
		console.log(item);
		var row = '<tr>'
		+ '<td>' + item.barcode + '</td>'
		+ '<td>' + item.quantity + '</td>'
		+ '<td>' + item.sellingPrice + '</td>'
		+ '<td>' + item.sellingPrice*item.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	$('#view-order-modal').modal('toggle');
}


function displayEditOrder(id){
	var url = getOrderUrl() + '/' + id + '/items';
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderForm(data, id);   
	   },
	   error: handleAjaxError
	});	
}

function displayOrderForm(data, id){
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
