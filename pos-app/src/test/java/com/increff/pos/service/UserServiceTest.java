package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.UserPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AbstractUnitTest {

    private static final String email = "email@abc.com";
    private static final String password = "password";
    @Autowired
    private UserService userService;

    @Test(expected = ApiException.class)
    public void testRepeatedAdd() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(email);
        userPojo.setPassword(password);
        userService.add(userPojo);
    }

    @Before
    public void initUserService() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(email);
        userPojo.setPassword(password);
        userService.add(userPojo);
    }
}
