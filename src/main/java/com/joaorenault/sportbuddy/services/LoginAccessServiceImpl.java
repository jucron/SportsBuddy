package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class LoginAccessServiceImpl implements LoginAccessService {
    LoginRepository loginRepository;

    public LoginAccessServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Set<LoginAccess> getLogins() {
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);
        return logins;
    }

    @Override
    public LoginAccess findLoginByUser(User userToFind) {
        Iterable<LoginAccess> logins = loginRepository.findAll();
        for (LoginAccess login :logins) {
            if (login.getUser()==userToFind) {
                return login;
            }
        }
        //User not found:
        return null; //todo
    }

    @Override
    public LoginAccess saveLogin(LoginAccess loginToSave) {
        LoginAccess savedLogin = loginRepository.save(loginToSave);
        return savedLogin;
    }

    @Override
    public void deleteLoginByUser(User userToDelete) {
        Iterable<LoginAccess> logins = loginRepository.findAll();
        for (LoginAccess login :logins) {
            if (Objects.equals(login.getUser().getId(), userToDelete.getId())) {
                loginRepository.deleteById(login.getId());
            }
        }
        //User not found:
        //todo
    }

    @Override
    public boolean checkExistentUsername(LoginAccess login) {
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);
        for (LoginAccess loginCheck : logins) {
            if (Objects.equals(loginCheck.getUsername(), login.getUsername())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long processLogin(LoginAccess login) {
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);
        for (LoginAccess loginCheck : logins) {
            if (Objects.equals(loginCheck.getUsername(), login.getUsername()) &&
                    Objects.equals(loginCheck.getPassword(), login.getPassword())) {
                return loginCheck.getUser().getId();
            }
        }
        return null;
    }
}
