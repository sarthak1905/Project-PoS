package com.increff.pos.controller;

import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() throws ApiException {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() throws ApiException {
		return mav("user.html");
	}

	@RequestMapping(value = "/ui/brands")
	public ModelAndView brand() throws ApiException {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/products")
	public ModelAndView product() throws ApiException {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() throws ApiException {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() throws ApiException {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/reports/day-sales")
	public ModelAndView daySalesReport() throws ApiException {
		return mav("day-sales.html");
	}

	@RequestMapping(value = "/ui/reports/filtered-sales")
	public ModelAndView filteredSalesReport() throws ApiException {
		return mav("filtered-sales.html");
	}

	@RequestMapping(value = "/ui/reports/brand")
	public ModelAndView brandsReport() throws ApiException {
		return mav("brand-report.html");
	}

	@RequestMapping(value = "/ui/reports/inventory")
	public ModelAndView inventoryReport() throws ApiException {
		return mav("inventory-report.html");
	}


}
