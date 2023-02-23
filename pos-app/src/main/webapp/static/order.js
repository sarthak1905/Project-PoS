function getOrderUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/orders";
}

function getProductUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/products";
}

var barcodes = new Set();

//BUTTON ACTIONS
function addOrder(event) {
  //Set the values to update
  var $form = $("#add-order-form");
  var jsonList = convertOrderForm($form);
  var url = getOrderUrl();

  $.ajax({
    url: url,
    type: "POST",
    data: JSON.stringify(jsonList),
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      $("#add-order-modal").modal("toggle");
      message = "Order placed successfully!";
      showSuccessMessage(message);
    },
    error: function(response) {
      handleAjaxError(response);
    }
  });

  return false;
}

function convertOrderForm($form) {
  var serializedData = $form.serializeArray();
  console.log(serializedData);
  var listLength = (serializedData.length - 3) / 3;
  var jsonList = [];
  
  for (let i = 1; i <= listLength; i++) {
    var jsonData = {};
    for (let j = i * 3; j < i * 3 + 3; j++) {
      jsonData[serializedData[j]["name"]] = serializedData[j]["value"];
    }
    jsonList.push(jsonData);
  }
  return jsonList;
}

function updateOrder(event) {
  //Gets the ID
  var id = $("#edit-order-id").html();
  var url = getOrderUrl() + "/" + id;

  //Set the values to update
  var $form = $("#edit-order-form");
  var jsonList = convertOrderForm($form);

  $.ajax({
    url: url,
    type: "PUT",
    data: JSON.stringify(jsonList),
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      message = "Order updated successfully!";
      showSuccessMessage(message);
      $("#edit-order-modal").modal("toggle");
    },
    error: function(response) {
      handleAjaxError(response);
    }
  });

  return false;
}

function initOrderItemRow() {
  barcodes.clear();
  populateBarcodeSet();
  $("#edit-order-tbody tr:not(:first-child)").remove();
  $("#add-order-tbody tr:not(:first-child)").remove();
  $('tr.add-order-row:first').clone().insertAfter("tr.add-order-row:last");
  var $selectField = $("#add-order-table").find(
    "tbody tr:last td select"
  );
  showBarcodeDropdown($selectField);
  $('tr.add-order-row:last').removeAttr('hidden');
  $("tr.add-order-row:last button").click(removeOrderItem);
}

function removeOrderItem() {
  var tableId = $(this).closest("table").attr("id");
  var rowCount = $("#" + tableId + " tr").length - 1;
  if (rowCount == 2) {
    message = "Minimum 1 order item required!";
    showErrorMessage(message);
    return;
  }
  var $rowToDelete = $(this).closest('tr');
  var $lastRow = $('#' + tableId + ' tr:last');
  if(!$rowToDelete.is($lastRow)){
    var selectedField = $rowToDelete.find('td select').select2('data');
    console.log(selectedField);
    var selectedField0 = selectedField[0];
    console.log(selectedField0);
    var barcode = selectedField0.text;
    console.log(barcode);
    barcodes.add(barcode);
    showBarcodeDropdown($lastRow.find('td select'));
  } 
  $rowToDelete.remove();
}

function addOrderItemRow() {
  var $rowToClone = $("tr.add-order-row:first");
  var $lastRow = $('tr.add-order-row:last');
  var lastBarcode = $lastRow.find('td select').select2('data')[0].text;
  barcodes.delete(lastBarcode);
  $rowToClone.clone().insertAfter("tr.add-order-row:last");
  var $newRow = $("tr.add-order-row:last");
  var $newRowDropdown = $("tr.add-order-row:last td select");
  showBarcodeDropdown($newRowDropdown);
  $newRowDropdown.select2();
  $newRow.removeAttr('hidden');
  $lastRow.find('td select').attr('readonly', true);
  $("tr.add-order-row:last input[name=quantity]").val("");
  $("tr.add-order-row:last input[name=sellingPrice]").val("");
  $("tr.add-order-row:last button").click(removeOrderItem);
}

function editAddOrderItemRow() {
  var $rowToClone = $("tr.edit-order-row:first");
  var $lastRow = $("tr.edit-order-row:last");
  var lastBarcode = $lastRow.find('td select').select2('data')[0].text;
  barcodes.delete(lastBarcode);
  $rowToClone.clone().insertAfter("tr.edit-order-row:last");
  var $newRow = $("tr.edit-order-row:last");
  var $newRowDropdown = $("tr.edit-order-row:last td select");
  showBarcodeDropdown($newRowDropdown);
  $newRow.find('td select').attr('readonly', false);
  $newRow.find('td select').select2();
  $newRow.removeAttr('hidden');
  $lastRow.find('td select').attr('readonly', true);
  $("tr.edit-order-row:last input[name=quantity]").val("");
  $("tr.edit-order-row:last input[name=sellingPrice]").val("");
  $("tr.edit-order-row:last button").replaceWith(
    '<button type="button" class="btn btn-remove button"><i class="bi bi-trash"></i> Remove</button>'
  );
  $("tr.edit-order-row:last button").click(removeOrderItem);
}

function populateBarcodeSet() {
  var url = getProductUrl();
  $.ajax({
    url: url,
    type: "GET",
    async: false,
    success: function (data) {
        for(var i in data){
          var product = data[i];
          barcodes.add(product.barcode);
        }
    },
    error: function(response) {
      handleAjaxError(response);
    }
  });
}


function showBarcodeDropdown(element) {
  var $selectBarcodeInput = element;
  $selectBarcodeInput.empty();
  for (barcode of barcodes.values()) {
    $selectBarcodeInput.append(
      $("<option></option>").attr("value", barcode).text(barcode)
    );
  }
  $selectBarcodeInput.select2();
}

function getOrderList() {
  var url = getOrderUrl() + '/filtered';
	var $form = $("#filter-form");
	var json = toJson($form);

  $.ajax({
    url: url,
    type: "POST",
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},	
    success: function (data) {
      destroyTablize();
      displayOrderList(data);
      dataTablize();
      $('#order-table').removeAttr('hidden');
    },
    error: function(response) {
      handleAjaxError(response);
    }
  });
}

function downloadOrderInvoice(id) {
  var url = getOrderUrl();
  url += "/" + id + "/invoice";

  $.ajax({
    url: url,
    type: "GET",
    success: function(){
      message = 'Please check your downloads for the invoice';
      showSuccessMessage(message);
      $("#btn-edit" + id).remove();
      $("#btn-invoice" + id).html('<i class="bi bi-cloud-arrow-down-fill"></i> Download');
      window.location.href = url;
    },
    error: function(response){
      message = "There was a problem generating the invoice, please try again later";
      showErrorMessage(message);
    }
  })

}

//UI DISPLAY METHODS

function displayOrderList(data) {
  var $tbody = $("#order-table").find("tbody");
  $tbody.empty();
  for (var i in data) {
    var o = data[i];
    var downloadIncvoiceButton = "";
    var invoiceStatus = "";
    var actionsButton =
      ' <button id="btn-view' +
      o.id +
      '"class="btn btn-view button" onclick="displayOrderItems(' +
      o.id +
      ')"><i class="bi bi-eye-fill"></i> View</button>';
    if (o.invoiced === false) {
      invoiceStatus += "Not Invoiced";
      downloadIncvoiceButton +=
        ' <button id="btn-invoice' +
        o.id +
        '"class="btn btn-view button" onclick="downloadOrderInvoice(' +
        o.id +
        ')"><i class="fas fa-sync-alt"></i> Generate</button>';
      actionsButton +=
        ' <button id="btn-edit' +
        o.id +
        '"class="btn btn-edit button order-add" onclick="displayEditOrder(' +
        o.id +
        ')"><i class="bi bi-pen-fill"></i> Edit</button>';
    } else {
      invoiceStatus += "Invoiced";
      downloadIncvoiceButton +=
        ' <button id="btn-invoice' +
        o.id +
        '"class="btn btn-view button" onclick="downloadOrderInvoice(' +
        o.id +
        ')"><i class="bi bi-cloud-arrow-down-fill"></i> Download</button>';
    }
    var row =
      "<tr>" + 
	    "<td>" + o.id + "</td>" +
      "<td>" + o.dateTime + "</td>" +
      "<td>" + o.orderTotal.toFixed(2) + "</td>" +
      "<td>" + invoiceStatus + "</td>" +
      "<td>" + actionsButton + "</td>" +
      "<td>" + downloadIncvoiceButton + "</td>" +
      "</tr>";
    $tbody.append(row);
  }
}

function displayOrderItems(id) {
  var url = getOrderUrl() + "/" + id + "/items";
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      viewOrderItems(data, id);
    },
    error: function(response){
      handleAjaxError(response);
    } 
  });
}

function viewOrderItems(data, id) {
  $("#view-order-id").html(id);
  var $tbody = $("#view-order-tbody");
  $tbody.empty();
  var orderTotal = 0.0;
  for (var i in data) {
    var item = data[i];
    var itemTotal = item.sellingPrice * item.quantity;
    var row =
      "<tr>" + 
      "<td>" + item.barcode + "</td>" +
      "<td>" + item.productName + "</td>" +
      "<td>" + item.quantity + "</td>" +
      "<td>" + item.sellingPrice.toFixed(2) + "</td>" +
      "<td>" + itemTotal.toFixed(2) + "</td>" +
      "</tr>";
      orderTotal += itemTotal;
    $tbody.append(row);
  }
  $('#view-order-total').html(orderTotal.toFixed(2));
  $("#view-order-modal").modal("toggle");
}

function displayEditOrder(id) {
  barcodes.clear();
  populateBarcodeSet();
  var url = getOrderUrl() + "/" + id + "/items";
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayOrderForm(data, id);
    },
    error: function(response){
      handleAjaxError(response);
    } 
  });
}

function displayOrderForm(data, id) {
  $("#edit-order-id").html(id);
  var $editTbody = $("#edit-order-tbody");
  var orderTotal = 0.0;
  $("#edit-order-tbody tr:not(:first-child)").remove();
  for (let i = 0; i < data.length; i++) {
    $editTbody.find("tr.edit-order-row:first").clone().insertAfter("tr.edit-order-row:last");
    $row = $editTbody.find("tr:last");
    $row.removeAttr('hidden');
    $row.find("td select").empty();

    $row.find("td select").append('<option>'+ data[i].barcode +'</option>');
    $row.find("td select").val(data[i].barcode);
    $row.find("td select").select2();
    $row.find("td select").attr('readonly', true);
    barcodes.delete(data[i].barcode);
    
    $row.find("td div input[name=quantity]").val(data[i].quantity);
    $row.find("td div input[name=sellingPrice]").val(data[i].sellingPrice.toFixed(2));
    var itemTotal = data[i].quantity * data[i].sellingPrice;
    orderTotal += itemTotal;
  }
  $('#edit-order-total').html(orderTotal.toFixed(2));
  $("#edit-order-modal").modal("toggle");
}

//INITIALIZATION CODE
function init() {
  $("#add-order-item").click(addOrderItemRow);
  $("#edit-add-order-item").click(editAddOrderItemRow);
  $("#add-order-dialog").click(initOrderItemRow);
  $("#add-order").click(addOrder);
  $("#update-order").click(updateOrder);
  $("#filter-btn").click(getOrderList);
  checkRoleAndDisableEditBtns();
}

$(document).ready(init);
