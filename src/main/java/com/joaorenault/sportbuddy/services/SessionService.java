package com.joaorenault.sportbuddy.services;

public final class SessionService {
    public Long sessionUserID;

    private SessionService() {
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
}
