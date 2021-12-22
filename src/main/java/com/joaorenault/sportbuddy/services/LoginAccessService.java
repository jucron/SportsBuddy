package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;

import java.util.Set;

public interface LoginAccessService {
    Set<LoginAccess> getLogins();
    LoginAccess findLoginByUser(User user);
    LoginAccess encodePassAndSaveLogin(LoginAccess login);
    void deleteLoginByUser(User user);
    boolean checkExistentUsername (LoginAccess login);
    Long processLogin (LoginAccess login);
    LoginAccess findLoginByUsername(String username);

    LoginAccess saveLogin(LoginAccess login);
}
