
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
	   },
	   error: handleAjaxError
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
		+ '<td>' + d.totalRevenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	//$('#refresh-data').click(getDaySalesList);
}

$(document).ready(init);
$(document).ready(getDaySalesList);
