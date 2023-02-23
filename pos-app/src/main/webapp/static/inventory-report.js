
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/reports/inventory";
}

function getInventoryList(){
	var url = getInventoryReportUrl();
	var $form = $("#filter-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	
	   success: function(data) {
			destroyTablize();
	   		displayInventoryList(data);
			dataTablize();
			$('#inventory-report-table').removeAttr('hidden');
	   },
	   error: function(response){
			handleAjaxError(response);
	   }
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
	$('#filter-btn').click(getInventoryList);
	displayOrHideButtons();
}

$(document).ready(init);
