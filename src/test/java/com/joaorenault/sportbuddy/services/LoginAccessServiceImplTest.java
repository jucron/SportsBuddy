package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoginAccessServiceImplTest {

    LoginAccessServiceImpl loginAccessService;

    @Mock
    LoginRepository loginRepository;

    @BeforeEach
    void setUp() {
        //initialize Mocks
        MockitoAnnotations.initMocks(this); //deprecated
        loginAccessService = new LoginAccessServiceImpl(loginRepository);

    }

    @Test
    void getLogins() {
        //Creating data:
        LoginAccess johnLogin = new LoginAccess("john","examplePass");
        johnLogin.setId(1L);
        List<LoginAccess> loginAccessList = new ArrayList<>();
        loginAccessList.add(johnLogin);

        //Mocking repository use:
        when(loginRepository.findAll()).thenReturn(loginAccessList);

        //testing method
        Set<LoginAccess> logins = loginAccessService.getLogins();

        //Asserting results:
        assertNotNull(logins);
        assertEquals(1,logins.size());
        verify(loginRepository, times(1)).findAll();
        verify(loginRepository, never()).findById(anyLong());
    }

    @Test
    void findLoginByUser() {
        //Creating data:
        LoginAccess johnLogin = new LoginAccess("john","examplePass");
        johnLogin.setId(1L);
        User john = new User(johnLogin,"John Master","john@email.com");
        johnLogin.setUser(john);
        List<LoginAccess> loginAccessList = new ArrayList<>();
        loginAccessList.add(johnLogin);

        //Mocking repository use:
        when(loginRepository.findAll()).thenReturn(loginAccessList);

        //testing method
        LoginAccess login = loginAccessService.findLoginByUser(john);

        //Asserting results:
        assertNotNull(login);
        assertEquals(1L,login.getId());
        assertEquals("examplePass",login.getPassword());
        verify(loginRepository, times(1)).findAll();
        verify(loginRepository, never()).findById(anyLong());
    }

    @Test
    void checkExistentUsername() {
    }

    @Test
    void processLogin() {
    }
}