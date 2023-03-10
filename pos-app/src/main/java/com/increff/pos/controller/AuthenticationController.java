package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
public class AuthenticationController extends AbstractUiController {

	@Autowired
	private UserDto userDto;
	@Autowired
	private InfoData info;


	@ApiOperation(value = "Initializes application")
	@RequestMapping(path = "/site/signup", method = RequestMethod.POST)
	public void signupUser(@RequestBody UserForm userForm) throws ApiException {
		userDto.add(userForm);
	}

	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
		if (!userDto.authenticate(loginForm)) {
			return new ModelAndView("redirect:/site/login");
		}
		userDto.createLoginSession(loginForm, req);
		return new ModelAndView("redirect:/ui/orders");

	}

	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}

}
