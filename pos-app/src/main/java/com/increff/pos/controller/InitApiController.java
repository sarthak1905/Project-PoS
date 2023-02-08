package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class InitApiController extends AbstractUiController {

	@Autowired
	private UserDto userDto;
	@Autowired
	private InfoData info;

	@ApiOperation(value = "Initializes application")
	@RequestMapping(path = "/site/signup", method = RequestMethod.POST)
	public ModelAndView signupUser(UserForm userForm) throws ApiException {
		userDto.add(userForm);
		info.setMessage("Signed up successfully!");
		return new ModelAndView("redirect:/site/login");
	}

}
