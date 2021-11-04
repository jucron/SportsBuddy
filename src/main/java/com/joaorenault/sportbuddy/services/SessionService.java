package com.joaorenault.sportbuddy.services;

public interface SessionService {

    Long getSessionUserID();
    void setSessionUserID(Long sessionUserID);
    String getSessionUserName();
    void setSessionUserName(String sessionUserName);
}
