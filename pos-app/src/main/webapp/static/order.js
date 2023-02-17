function getOrderUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/orders";
}

function getProductUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/products";
}

//BUTTON ACTIONS
function addOrder(event) {
  //Set the values to update
  var $form = $("#add-order-form");
  var jsonList = convertOrderForm($form);
  console.log(jsonList);
  var url = getOrderUrl();

  $.ajax({
    url: url,
    type: "POST",
    data: JSON.stringify(jsonList),
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      refreshTable();
      $("#add-order-modal").modal("toggle");
      message = "Order placed successfully!";
      showSuccessMessage(message);
    },
    error: handleAjaxError,
  });

  return false;
}

function convertOrderForm($form) {
  var serializedData = $form.serializeArray();
  console.log('Printing serialized data...');
  console.log(serializedData);
  var listLength = serializedData.length / 3;
  var jsonList = [];
  for (let i = 1; i < listLength; i++) {
    var jsonData = {};
    for (let j = i * 3; j < i * 3 + 3; j++) {
      jsonData[serializedData[j]["name"]] = serializedData[j]["value"];
    }
    jsonList.push(jsonData);
  }
  return jsonList;
}

function updateOrder(event) {
  $("#edit-order-modal").modal("toggle");
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
      refreshTable();
      message = "Order updated successfully!";
      showSuccessMessage(message);
    },
    error: handleAjaxError,
  });

  return false;
}

function removeOrderItem() {
  var tableId = $(this).closest("table").attr("id");
  var rowCount = $("#" + tableId + " tr").length - 1;
  if (rowCount == 2) {
    alert("Minimum 1 order item required!");
    return;
  }
  $(this).closest("tr").remove();
}

function addOrderItemRow() {
  var $tbody = $("#add-order-tbody");
  var $rowToClone = $("tr.add-order-row:first");
  $rowToClone.clone().insertAfter("tr.add-order-row:last");
  var $newRow = $("tr.add-order-row:last");
  var $newRowDropdown = $("tr.add-order-row:last td select");
  getProductList($newRowDropdown, (add = true));
  $newRowDropdown.select2();
  $newRow.removeAttr('hidden');
  $("tr.add-order-row:last input[name=quantity]").val("");
  $("tr.add-order-row:last input[name=sellingPrice]").val("");
  $("tr.add-order-row:last button").click(removeOrderItem);
}

function editAddOrderItemRow() {
  var $tbody = $('#edit-order-tbody');
  var $rowToClone = $("tr.edit-order-row:first");
  $rowToClone.clone().insertAfter("tr.edit-order-row:last");
  var $newRow = $("tr.edit-order-row:last");
  var $newRowDropdown = $("tr.edit-order-row:last td select");
  getProductList($newRowDropdown, (add = false));
  $newRow.find('td select').removeAttr('readonly');
  $newRow.find('td select').select2();
  $newRow.removeAttr('hidden');
  $("tr.edit-order-row:last input[name=quantity]").val("");
  $("tr.edit-order-row:last input[name=sellingPrice]").val("");
  $("tr.edit-order-row:last button").replaceWith(
    '<button type="button" class="btn btn-remove button">Remove</button>'
  );
  $("tr.edit-order-row:last button").click(removeOrderItem);
}

function getProductList(element, isAdd) {
  var url = getProductUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      console.log(data);
      if (isAdd) {
        showBarcodeDropdownAdd(data, element);
      } else {
        showBarcodeDropdownEdit(data, element);
      }
    },
    error: handleAjaxError,
  });
}

function showBarcodeDropdownAdd(data, element) {
  const barcodes = new Set();
  var $selectBarcodeInput = element;
  $selectBarcodeInput.empty();

  for (var i in data) {
    var productDetails = data[i];
    barcodes.add(productDetails.barcode);
  }

  for (barcode of barcodes.values()) {
    $selectBarcodeInput.append(
      $("<option></option>").attr("value", barcode).text(barcode)
    );
  }
  $selectBarcodeInput.select2();
}

function showBarcodeDropdownEdit(productData, element) {
  var $selectBarcodeInput = element;
  $selectBarcodeInput.empty();

  getUniqueBarcodes(productData, element);
}

function getUniqueBarcodes(productData, $selectBarcodeInput) {
  var id = $("#edit-order-id").html();
  var url = getOrderUrl() + "/" + id + "/items";
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      const barcodes = new Set();
      var map = new Map();
      for (var i in data) {
        var orderItemDetails = data[i];
        map.set(orderItemDetails.barcode, 1);
      }
      for (var i in productData) {
        var productDetials = productData[i];
        if (map.get(productDetials.barcode) == undefined) {
          barcodes.add(productDetials.barcode);
        }
      }
      if (barcodes != undefined) {
        for (barcode of barcodes.values()) {
          $selectBarcodeInput.append(
            $("<option></option>").attr("value", barcode).text(barcode)
          );
        }
      }
    },
    error: handleAjaxError,
  });
}

function initOrderItemRow() {
  $("#edit-order-tbody tr:not(:first-child)").remove();
  $('tr.add-order-row:first').clone().insertAfter("tr.add-order-row:last");
  var $selectField = $("#add-order-table").find(
    "tbody tr:last td select"
  );
  getProductList($selectField, isAdd=true);
  $('tr.add-order-row:last').removeAttr('hidden');
}

function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayOrderList(data);
      dataTablize();
    },
    error: handleAjaxError,
  });
}

function downloadOrderInvoice(id) {
  var url = getOrderUrl();
  url += "/" + id + "/invoice";
  $("#btn-edit" + id).remove();
  window.location.href = url;
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
      "<td>" + o.orderTotal + "</td>" +
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
    error: handleAjaxError,
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
      "<td>" + item.quantity + "</td>" +
      "<td>" + item.sellingPrice + "</td>" +
      "<td>" + itemTotal + "</td>" +
      "</tr>";
      orderTotal += itemTotal;
    $tbody.append(row);
  }
  $('#view-order-total').html(orderTotal);
  $("#view-order-modal").modal("toggle");
}

function displayEditOrder(id) {
  var url = getOrderUrl() + "/" + id + "/items";
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayOrderForm(data, id);
    },
    error: handleAjaxError,
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
    
    $row.find("td div input[name=quantity]").val(data[i].quantity);
    $row.find("td div input[name=sellingPrice]").val(data[i].sellingPrice);
    var itemTotal = data[i].quantity * data[i].sellingPrice;
    orderTotal += itemTotal;
  }
  $('#edit-order-total').html(orderTotal);
  $("#edit-order-modal").modal("toggle");
}

function refreshTable(){
	destroyTablize();
	getOrderList();
}

//INITIALIZATION CODE
function init() {
  $("#add-order-item").click(addOrderItemRow);
  $("#edit-add-order-item").click(editAddOrderItemRow);
  $("#add-order-dialog").click(initOrderItemRow);
  $("#add-order").click(addOrder);
  $("#add-order-first-row-btn").click(removeOrderItem);
  $("#update-order").click(updateOrder);
  $("#refresh-data").click(refreshTable);
  checkRoleAndDisableEditBtns();
}

$(document).ready(init);
$(document).ready(getOrderList);
