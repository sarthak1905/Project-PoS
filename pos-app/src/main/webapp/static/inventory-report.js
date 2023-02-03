
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/reports/inventory";
}

function getInventoryList(){
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

function displayInventoryList(data){
	var $tbody = $('#inventory-report-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		var row = '<tr>'
		+ '<td>' + b.brand + '</td>'
		+ '<td>' + b.category + '</td>'
		+ '<td>'  + b.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function init(){
	$('#refresh-btn').click(getInventoryList);
}

$(document).ready(init);
$(document).ready(getInventoryList);
