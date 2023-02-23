
function getDaySalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/day-sales";
}

function getDaySalesList(){
	var url = getDaySalesReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayDaySalesList(data);
			dataTablize();
	   },
	   error: function(response) {
			handleAjaxError(response);
	   }
	});
}

function getFilteredList(){
	var url = getDaySalesReportUrl() + '/filter';
	var $form = $("#filter-form");
	var json = toJson($form);
	
	$.ajax({
		url: url,
		type: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},	
		data: json,
		success: function(data) {
			displayDaySalesList(data);
			dataTablize();
		},
		error: function(response) {
			handleAjaxError(response);
	   }
	 });
}

//UI DISPLAY METHODS

function displayDaySalesList(data){
	var $tbody = $('#day-sales-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var d = data[i];
		var row = '<tr>'
		+ '<td>' + d.date + '</td>'
		+ '<td>' + d.invoicedOrdersCount + '</td>'
		+ '<td>'  + d.invoicedItemsCount + '</td>'
		+ '<td>' + d.totalRevenue.toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	//$('#refresh-data').click(getDaySalesList);
	$('#filter-report-btn').click(getFilteredList);
}

$(document).ready(init);
$(document).ready(getDaySalesList);
