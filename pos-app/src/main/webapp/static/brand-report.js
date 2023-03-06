
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/brand";
}

function getBrandUrl(){
	var baseUrl = $('meta[name=baseUrl]').attr('content')
	return baseUrl + '/api/brands';
}

const brandCategoriesMap = {};

function generateBrandCategoriesMap(){
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(brandData) {
			var all = "---All---";
			brandCategoriesMap[all] = [all];
			for(var i in brandData){
				var tempBrand = brandData[i];
				brandCategoriesMap[all].push(tempBrand.category);
				if(tempBrand.brand in brandCategoriesMap){
					brandCategoriesMap[tempBrand.brand].push(tempBrand.category); 
				}
				else{
					brandCategoriesMap[tempBrand.brand] = [all];
					brandCategoriesMap[tempBrand.brand].push(tempBrand.category);
				}
			}
			showBrandDropdown(Object.keys(brandCategoriesMap)[0], true);
		},
		error: function(response){
			handleAjaxError(response);
		}
	});
}

function showBrandDropdown(brand, firstRun){
	const brandNames = new Set();
	const categoryNames = new Set();
	
	var selectCategoryString = '#category-name';
	var selectBrandString = '#brand-name';

	var $selectCategoryName = $(selectCategoryString);
	$selectCategoryName.empty();

	var $selectBrandName = $(selectBrandString);
	if(firstRun){
		$selectBrandName.empty();
	}

	for(const [key, value] of Object.entries(brandCategoriesMap)){
		if(firstRun){
			brandNames.add(key);
		}
		if(key === brand){
			for(const category of value){
				categoryNames.add(category);
			}
		}
	}

	for(categoryName of categoryNames.values()){
		var option1 = $('<option></option>').attr("value", categoryName).text(categoryName);
        $selectCategoryName.append(option1);
	}

	for(brandName of brandNames.values()){
		var option2 = $('<option></option>').attr("value", brandName).text(brandName);
		$selectBrandName.append(option2);
	}

	if(firstRun){
		$selectBrandName.select2({ width: '100%' });
		$selectCategoryName.select2({ width: '100%' });
	}
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
	$('#brand-name').on('select2:select', function(e){
		var data = e.params.data.text;
		showBrandDropdown(data, firstRun=false);
	});
	getBrandList();
}

$(document).ready(init);
$(document).ready(generateBrandCategoriesMap);