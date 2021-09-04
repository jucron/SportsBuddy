package com.joaorenault.sportbuddy.services;

public class SessionService {

    private static Long sessionUserID;

    private static String sessionUserName;

    public SessionService() {
    }

    public SessionService(Long sessionUserID) {
        this.sessionUserID = sessionUserID;
    }

    public Long getSessionUserID() {
        return sessionUserID;
    }

    public void setSessionUserID(Long sessionUserID) {
        this.sessionUserID = sessionUserID;
    }

    public static String getSessionUserName() {
        return sessionUserName;
    }

    public static void setSessionUserName(String sessionUserName) {
        SessionService.sessionUserName = sessionUserName;
    }
}
