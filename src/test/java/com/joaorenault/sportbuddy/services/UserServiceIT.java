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
import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceIT {

    @Autowired
    UserService userService;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    void saveUser() {

        //Creating data:
        LoginAccess lucasLogin = new LoginAccess("lucas","examplePass");
        User lucas = new User(lucasLogin,"Lucas Master","lucas@email.com");

        //testing method:
        loginRepository.save(lucasLogin); //Persisting and creating a unique ID
        userService.saveUser(lucas); // *TEST* Persisting and creating a unique ID
        lucasLogin.setUser(lucas);
        User userSaved = userService.saveUser(lucas); //*TEST*Persisting with the correct user

        //Asserting results:
        TreeSet<User> users = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        userRepository.findAll().iterator().forEachRemaining(users::add);

        assertNotNull(userSaved);
        assertEquals("Lucas Master",userSaved.getName());
        assertEquals("lucas@email.com",userSaved.getEmail());
        assertEquals((4+1),users.size()); //Bootstrap (4) + newUser (1)
        assertTrue(users.iterator().hasNext());
    }

    @Test
    @Transactional
    void deleteUserByIdFound() {
        //Creating data:
        LoginAccess biancaLogin = new LoginAccess("bianca","examplePass");
        loginRepository.save(biancaLogin); //Persisting and creating a unique ID
        User bianca = new User(biancaLogin,"Bianca Master","bianca@email.com");
        userRepository.save(bianca);//Persisting and creating a unique ID
        //Associating Login and User:
        biancaLogin.setUser(bianca);
        loginRepository.save(biancaLogin);
        userRepository.save(bianca);

        //testing
        userService.deleteUserById(bianca.getId());

        //Asserting results:
        TreeSet<User> users = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        userRepository.findAll().iterator().forEachRemaining(users::add);

        assertNotNull(users);
        assertEquals((4+1-1),users.size()); //Bootstrap (4) + newUser (1) - newUser (1)
        assertFalse(users.contains(bianca));
    }

}
