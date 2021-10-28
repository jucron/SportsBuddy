package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//Integration test
@RunWith(SpringRunner.class)
@SpringBootTest
class LoginAccessServiceIT {

    @Autowired
    LoginAccessServiceImpl loginAccessService;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void saveLogin() {
        //Creating data:
        LoginAccess lucasLogin = new LoginAccess("lucas","examplePass");

        //testing method
        LoginAccess savedLogin = loginAccessService.saveLogin(lucasLogin);
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);

        //Asserting results:
        assertNotNull(savedLogin);
        assertEquals("lucas",savedLogin.getUsername());
        assertEquals("examplePass",savedLogin.getPassword());
        assertEquals((4+1),logins.size()); //Bootstrap have already 4 logins
        assertTrue(logins.iterator().hasNext());
    }

    @Test
    void deleteLoginByUser() {
        //Creating data:
        LoginAccess biancaLogin = new LoginAccess("bianca","examplePass");
        loginRepository.save(biancaLogin); //Persisting and creating a unique ID
        User bianca = new User(biancaLogin,"Bianca Master","bianca@email.com");
        userRepository.save(bianca);//Persisting and creating a unique ID
        //Associating Login and User:
        biancaLogin.setUser(bianca);
        loginRepository.save(biancaLogin);
        userRepository.save(bianca);

        //testing method
        loginAccessService.deleteLoginByUser(bianca);
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);
        for (LoginAccess login : logins) {
            System.out.println(login.getUsername());
        }
        //Asserting results:
        assertNotNull(logins);
        assertEquals((6-1),logins.size()); //Bootstrap (4) + saveLogin (1) + newUser (1)
        assertFalse(logins.contains(biancaLogin));
    }
}