package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.UserDao;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static junit.framework.TestCase.*;

public class UserDtoTest extends AbstractUnitTest {

    private static final String operatorEmail1 = "abc@example.com";
    private static final String operatorEmail2 = "supervisor1@gmail.com";
    private static final String password = "examplePassword";
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDto userDto;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    public void testGetRoleFromEmail() throws ApiException {
        List<UserPojo> userPojoList = userDao.selectAll();
        if(userPojoList.size() != 1){
            fail();
        }
        UserPojo userPojo = userPojoList.get(0);
        String role = userDto.getRoleFromEmail(userPojo.getEmail());
        assertEquals("operator", role);
    }

    @Test
    public void testConvertUserPojoToAuthentication() throws ApiException {
        List<UserPojo> userPojoList = userDao.selectAll();
        UserPojo userPojo = userPojoList.get(0);
        Authentication token = userDto.convertUserPojoToAuthentication(userPojo);
    }


    @Before
    public void initUser() throws ApiException {
        UserForm userForm = TestHelper.createUserForm(operatorEmail1, password);
        userDto.add(userForm);
    }
}
