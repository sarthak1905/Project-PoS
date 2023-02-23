
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/brand";
}

function getBrandList(){
	var url = getBrandReportUrl();
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
			displayBrandList(data);
			dataTablize();
			$('#brand-report-table').removeAttr('hidden');
		},
		error: function(response){
			 handleAjaxError(response);
		}
	 });
}

function displayBrandList(data){
	var $tbody = $('#brand-report-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var b = data[i];
		var row = '<tr>'
		+ '<td>' + b.brand + '</td>'
		+ '<td>'  + b.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#filter-btn').click(getBrandList);
	displayOrHideButtons();
}

$(document).ready(init);
