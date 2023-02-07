package com.increff.pos.controller;

import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteUiController extends AbstractUiController {

	// WEBSITE PAGES
	@RequestMapping(value = "")
	public ModelAndView index() throws ApiException {
		return mav("index.html");
	}

	@RequestMapping(value = "/site/login")
	public ModelAndView login() throws ApiException {
		return mav("login.html");
	}

	@RequestMapping(value = "/site/logout")
	public ModelAndView logout() throws ApiException {
		return mav("logout.html");
	}

}
