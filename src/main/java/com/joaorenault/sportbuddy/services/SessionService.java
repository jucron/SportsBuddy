package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;

public interface SessionService {

//    Long getSessionUserID();
//    void setSessionUserID(Long sessionUserID);
    String getSessionUserName();
//    void setSessionUserName(String sessionUserName);
    LoginAccess getLoginOfCurrentSession ();
}
