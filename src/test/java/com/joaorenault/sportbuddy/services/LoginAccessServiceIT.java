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

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//Integration test
@RunWith(SpringRunner.class)
@SpringBootTest
class LoginAccessServiceIT {

    @Autowired
    LoginAccessService loginAccessService;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void saveLogin() {
        //Creating data:
        LoginAccess lucasLogin = new LoginAccess("lucas","examplePass");
        User lucas = new User(lucasLogin,"Lucas Master","lucas@email.com");

        //testing method
        loginAccessService.encodePassAndSaveLogin(lucasLogin); // *TEST* Persisting and creating a unique ID
        userRepository.save(lucas); //Persisting and creating a unique ID
        lucasLogin.setUser(lucas);
        LoginAccess savedLogin = loginAccessService.encodePassAndSaveLogin(lucasLogin); //*TEST*Persisting with the correct user

        //Asserting results:
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);

        assertNotNull(savedLogin);
        assertEquals("lucas",savedLogin.getUsername());
        assertEquals("examplePass",savedLogin.getPassword());
        assertEquals((4+1),logins.size()); //Bootstrap (4) + newLogin (1)
        assertTrue(logins.iterator().hasNext());
    }

    @Test
    @Transactional
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

        //Asserting results:
        Set<LoginAccess> logins = new HashSet<>();
        loginRepository.findAll().iterator().forEachRemaining(logins::add);

        assertNotNull(logins);
        assertEquals((4+1-1),logins.size()); //Bootstrap (4) + newLogin (1) - newLogin (1)
        assertFalse(logins.contains(biancaLogin));
    }
}