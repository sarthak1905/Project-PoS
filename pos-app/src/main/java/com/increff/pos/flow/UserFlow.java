package com.increff.pos.flow;

import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserFlow {

    @Autowired
    private UserService userService;
    @Value("#{'${supervisor.emails}'.split(',')}")
    private List<String> supervisorEmails;


    public UserPojo get(String email) throws ApiException {
        return userService.get(email);
    }

    public void add(UserPojo userPojo) throws ApiException {
        ValidationUtil.checkPojo(userPojo);
        userService.add(userPojo);
    }

    public Authentication convertUserPojoToAuthentication(UserPojo userPojo) throws ApiException {
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

    public String getRoleFromPojo(UserPojo userPojo) throws ApiException {
        ValidationUtil.checkPojo(userPojo);
        if(supervisorEmails.contains(userPojo.getEmail())){
            return "supervisor";
        }
        return "operator";
    }

    public void createLoginSession(UserPojo userPojo, HttpServletRequest req) throws ApiException {
        // Create authentication object
        Authentication authentication = convertUserPojoToAuthentication(userPojo);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
    }
}
