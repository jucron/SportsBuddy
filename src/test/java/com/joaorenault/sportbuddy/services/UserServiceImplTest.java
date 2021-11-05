package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        //initialize Mocks
        MockitoAnnotations.initMocks(this); //deprecated
        userService = new UserServiceImpl(userRepository);

    }

    @Test
    void getUsers() {
        //Creating Data
        User john = new User(new LoginAccess(),"John Master","john@email.com");
        john.setId(1L);
        User lucas = new User(new LoginAccess(),"Lucas Master","lucas@email.com");
        lucas.setId(2L);
        TreeSet<User> usersRepertory = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        usersRepertory.add(john); usersRepertory.add(lucas); //repertory will have size of 2

        //Mocking repository use:
        when(userRepository.findAll()).thenReturn(usersRepertory);

        //testing method
        TreeSet<User> users = userService.getUsers();

        //assert results:
        assertNotNull(users);
        assertEquals(2,users.size());
        verify(userRepository, times(1)).findAll();
        assertEquals(john,users.first()); assertEquals(lucas,users.last()); //asserting sorting

    }

    @Test
    void findUserByIdIsFound() {
        User john = new User(new LoginAccess(),"John Master","john@email.com");
        john.setId(1L);

        //Mocking repository use:
        when(userRepository.findById(john.getId())).thenReturn(Optional.of(john));

        //testing
        User findUser = userService.findUserById(1L);

        //Assertions
        assertNotNull(findUser);
        assertEquals(john,findUser);
        verify(userRepository, times(1)).findById(anyLong());
    }
    @Test
    void findUserByIdNOTFound() {

        //Mocking repository use:
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        //testing
        Exception exception =
        assertThrows(RuntimeException.class, () -> {
            userService.findUserById(2L);
        });
        //asserting
        assertEquals("User not found", exception.getMessage());

    }
}