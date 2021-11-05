package com.joaorenault.sportbuddy.services;

import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private static Long sessionUserID;
    private static String sessionUserName;

    public SessionServiceImpl() {
    }

    public SessionServiceImpl(Long sessionUserID) {
        SessionServiceImpl.sessionUserID = sessionUserID;
    }

    @Override
    public Long getSessionUserID() {
        return sessionUserID;
    }
    @Override
    public void setSessionUserID(Long sessionUserID) {
        SessionServiceImpl.sessionUserID = sessionUserID;
    }

    @Override
    public String getSessionUserName() {
        return sessionUserName;
    }
    @Override
    public void setSessionUserName(String sessionUserName) {
        SessionServiceImpl.sessionUserName = sessionUserName;
    }
}
