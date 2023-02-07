package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserData;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {

	@Autowired
	private InfoData info;

	@Autowired
	private UserDto userDto;

	@Value("${app.baseUrl}")
	private String baseUrl;

	protected ModelAndView mav(String page) throws ApiException {
		// Get current user
		UserPrincipal principal = SecurityUtil.getPrincipal();

		info.setEmail(principal == null ? "" : principal.getEmail());

		UserData userData = new UserData();
		if(principal != null) {
			userData.setRole(userDto.getRoleFromEmail(principal.getEmail()));
		}
		else{
			userData.setRole("");
		}
		// Set info
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("info", info);
		mav.addObject("baseUrl", baseUrl);
		mav.addObject("user", userData);
		return mav;
	}

}
