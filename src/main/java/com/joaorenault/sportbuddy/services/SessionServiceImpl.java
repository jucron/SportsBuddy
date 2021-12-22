package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

//    private static Long sessionUserID;
//    private static String sessionUserName;
    private LoginAccessService loginAccessService;
    private String sessionUserName;

    public SessionServiceImpl(LoginAccessService loginAccessService) {
        this.loginAccessService = loginAccessService;
    }


//    public SessionServiceImpl(Long sessionUserID) {
//        SessionServiceImpl.sessionUserID = sessionUserID;
//    }

//    @Override
//    public Long getSessionUserID() {
//        return sessionUserID;
//    }
//    @Override
//    public void setSessionUserID(Long sessionUserID) {
//        SessionServiceImpl.sessionUserID = sessionUserID;
//    }

    @Override
    public String getSessionUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } catch (ClassCastException e) {
            log.info("User not logged in");
        }
        return null;
    }
//    @Override
//    public void setSessionUserName(String sessionUserName) {
//        this.sessionUserName = sessionUserName;
//    }
    @Override
    public LoginAccess getLoginOfCurrentSession () {
        return loginAccessService.findLoginByUsername(this.getSessionUserName());
    }
}
