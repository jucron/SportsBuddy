package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LoginAccessServiceImpl implements LoginAccessService, UserDetailsService {
    LoginRepository loginRepository;
    PasswordEncoder passwordEncoder;

    public LoginAccessServiceImpl(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override //From security core
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginAccess login = loginRepository.findByUsername(username);
        if (login==null) {
            log.info("User with username:'{}' not found in database",username);
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        //todo: in future implement roles/permissions to different users

        return new org.springframework.security.core.userdetails.User(
                login.getUsername(), login.getPassword(), authorities);
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
    public LoginAccess findLoginByUsername(String username) {
        //login not found: will return null
        return loginRepository.findByUsername(username);
    }

    @Override
    public LoginAccess saveLogin(LoginAccess login) {
        return loginRepository.save(login);
    }

    @Override
    public LoginAccess encodePassAndSaveLogin(LoginAccess loginToSave) {
        String encodedPass = passwordEncoder.encode(loginToSave.getPassword());
        loginToSave.setPassword(encodedPass);
        return loginRepository.save(loginToSave);
    }

    @Override
    public void deleteLoginByUser(User userToDelete) {
        Iterable<LoginAccess> logins = loginRepository.findAll();
        for (LoginAccess login :logins) {
            if (Objects.equals(login.getUser().getId(), userToDelete.getId())) {
                loginRepository.deleteById(login.getId());
                return;
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
