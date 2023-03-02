package com.increff.pos.dto;

import com.increff.pos.flow.UserFlow;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDto {

    @Autowired
    private UserFlow userFlow;
    @Autowired
    private InfoData info;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("#{'${supervisor.emails}'.split(',')}")
    private List<String> supervisorEmails;

    public String getRoleFromEmail(String email) throws ApiException {
        UserPojo userPojo = userFlow.get(email);
        return userFlow.getRoleFromPojo(userPojo);
    }

    public void add(UserForm userForm) throws ApiException {
        UserPojo userPojo = ConvertUtil.convertUserFormToUserPojo(userForm);
        userForm.setRole(userFlow.getRoleFromPojo(userPojo));
        userFlow.add(userPojo);
        info.setMessage("Signed up successfully!");
    }

    public Authentication convertUserPojoToAuthentication(UserPojo userPojo) throws ApiException {
        ValidationUtil.checkPojo(userPojo);
        return userFlow.convertUserPojoToAuthentication(userPojo);
    }

    public Boolean authenticate(LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userFlow.get(loginForm.getEmail());
        Boolean authenticated = (userPojo != null && passwordEncoder.matches(loginForm.getPassword(), userPojo.getPassword()));
        if(!authenticated)
            info.setMessage("Invalid username or password");
        return authenticated;
    }

    public void createLoginSession(LoginForm loginForm, HttpServletRequest req) throws ApiException {
        UserPojo userPojo = userFlow.get(loginForm.getEmail());
        userFlow.createLoginSession(userPojo, req);
    }
}
