
function getSalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sales";
}

function getSalesList(){
	var $form = $("#filter-form");
	var json = toJson($form);
	var url = getSalesUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   headers: {
		'Content-Type': 'application/json'
		},	
	   data: json,
	   success: function(data) {
			destroyTablize();
	   		displaySalesList(data);
			dataTablize();
			$('#sales-table').removeAttr('hidden');
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displaySalesList(data){
	var $tbody = $('#sales-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var s = data[i];
		var row = '<tr>'
		+ '<td>' + s.brand + '</td>'
		+ '<td>' + s.category + '</td>'
		+ '<td>' + s.quantity + '</td>'
		+ '<td>' + s.revenue.toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#generate-report-btn').click(getSalesList);
	displayOrHideButtons();
}

$(document).ready(init);
