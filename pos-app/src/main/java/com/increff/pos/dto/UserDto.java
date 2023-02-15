package com.increff.pos.dto;

import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDto {

    @Autowired
    private UserService userService;
    @Value("#{'${supervisor.emails}'.split(',')}")
    private List<String> supervisorEmails;

    public String getRoleFromEmail(String email) throws ApiException {
        UserPojo userPojo = userService.get(email);
        return getRoleFromPojo(userPojo);
    }

    public void add(UserForm userForm) throws ApiException {
        if(supervisorEmails.contains(userForm.getEmail())) {
            userForm.setRole("supervisor");
        }
        else {
            userForm.setRole("operator");
        }
        UserPojo userPojo = ConvertUtil.convertUserFormToUserPojo(userForm);
        userService.add(userPojo);
    }

    public List<UserPojo> getAll() {
        return userService.getAll();
    }

    public List<UserData> getAllUser() {
        List<UserPojo> list = getAll();
        List<UserData> list2 = new ArrayList<>();
        for (UserPojo userPojo : list) {
            String role = getRoleFromPojo(userPojo);
            list2.add(ConvertUtil.convertUserPojoToUserData(userPojo, role));
        }
        return list2;
    }

    public UserPojo get(LoginForm loginForm) throws ApiException {
        return userService.get(loginForm.getEmail());
    }

    public Authentication convertUserPojoToAuthentication(UserPojo userPojo) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getId());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getRoleFromPojo(userPojo)));
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

    private String getRoleFromPojo(UserPojo userPojo){
        if(supervisorEmails.contains(userPojo.getEmail())){
            return "supervisor";
        }
        return "operator";
    }
}
