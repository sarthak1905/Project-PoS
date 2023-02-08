package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        return userPojo.getRole();
    }

    public void add(UserForm userForm) throws ApiException {
        for(String email: supervisorEmails){
            System.out.println(email);
        }
        if(supervisorEmails.contains(userForm.getEmail())) {
            userForm.setRole("supervisor");
        }
        else {
            userForm.setRole("operator");
        }
        UserPojo userPojo = ConvertUtil.convertUserFormToUserPojo(userForm);
        userService.add(userPojo);
    }

    public void delete(Integer id) {
        userService.delete(id);
    }

    public List<UserPojo> getAll() {
        return userService.getAll();
    }

    public List<UserData> getAllUser() {
        List<UserPojo> list = getAll();
        List<UserData> list2 = new ArrayList<>();
        for (UserPojo userPojo : list) {
            list2.add(ConvertUtil.convertUserPojoToUserData(userPojo));
        }
        return list2;
    }

}
