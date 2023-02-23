
function getDaySalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/day-sales";
}

function getFilteredList(){
	var url = getDaySalesReportUrl();
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
			$('#day-sales-table').removeAttr('hidden');
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
	displayOrHideButtons();
}

$(document).ready(init);
