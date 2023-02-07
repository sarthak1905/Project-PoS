package com.increff.pos.dto;

import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDto {

    @Autowired
    private UserService userService;

    public String getRoleFromEmail(String email) throws ApiException {
        UserPojo userPojo = userService.get(email);
        return userPojo.getRole();
    }
}
