
function getSalesUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/sales";
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

function getInitSales(){
	var today = new Date();
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0'); 
	var yyyy = today.getFullYear();
  
	today = yyyy + '-' + mm + '-' + dd;
	$('#input-start-date').val(today);
	$('#input-end-date').val(today);
	getSalesList();
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
	$('#brand-name').on('select2:select', function(e){
		var data = e.params.data.text;
		showBrandDropdown(data, firstRun=false);
	});
	getInitSales();
}

$(document).ready(init);
$(document).ready(generateBrandCategoriesMap);
