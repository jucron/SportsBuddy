package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginAccessServiceImplTest {

    LoginAccessServiceImpl loginAccessService;

    @Mock
    LoginRepository loginRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        //initialize Mocks
        MockitoAnnotations.initMocks(this); //deprecated
        loginAccessService = new LoginAccessServiceImpl(loginRepository, passwordEncoder);

    }
    @Test
    void loadUserByUsername() {
        //Creating data:
        String username = "john";
        LoginAccess johnLogin = new LoginAccess("john","examplePass");

        //Mocking repository use:
        when(loginRepository.findByUsername(username)).thenReturn(johnLogin);

        //testing method
        UserDetails userDetails = loginAccessService.loadUserByUsername(username);

        //Asserting results:
        verify(loginRepository).findByUsername(anyString());
        assertEquals(johnLogin.getUsername(),userDetails.getUsername());
        assertEquals(johnLogin.getPassword(),userDetails.getPassword());
    }
    @Test
    void loadUserByUsernameReturnsNull() {
        //Creating data:
        String username = "john";

        //Mocking repository use:
        when(loginRepository.findByUsername(username)).thenReturn(null);

        //testing method
        Exception exception =
                assertThrows(UsernameNotFoundException.class, () -> {
                    loginAccessService.loadUserByUsername(username);
                });
        //asserting
        verify(loginRepository).findByUsername(anyString());
        assertEquals("User not found in database", exception.getMessage());
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
    void findLoginByUsername() {
        //Creating data:
        String existingUsername = "john";
        String nonExistingUsername = "larry";
        LoginAccess johnLogin = new LoginAccess(existingUsername,"examplePass");

        //Mocking repository use:
        when(loginRepository.findByUsername(existingUsername)).thenReturn(johnLogin);
        when(loginRepository.findByUsername(nonExistingUsername)).thenReturn(null);

        //testing method
        LoginAccess loginFound = loginAccessService.findLoginByUsername(existingUsername);
        LoginAccess loginNotFound = loginAccessService.findLoginByUsername(nonExistingUsername);

        //Asserting results:
        assertNotNull(loginFound);
        assertNull(loginNotFound);
        verify(loginRepository, times(2)).findByUsername(anyString());
        verify(loginRepository, never()).findById(anyLong());
    }
    @Test
    void saveLogin() {
        //Creating data:
        LoginAccess johnLogin = new LoginAccess("john","examplePass");

        //Mocking repository use:
        when(loginRepository.save(johnLogin)).thenReturn(johnLogin);

        //testing method
        LoginAccess saveLogin = loginAccessService.saveLogin(johnLogin);

        //Asserting results:
        verify(loginRepository).save(any(LoginAccess.class));
        assertNotNull(saveLogin);
    }

    @Test
    void checkExistentUsername() {
        //Creating data:
        LoginAccess johnLogin = new LoginAccess("john","examplePass");
        User john = new User(johnLogin,"John Master","john@email.com");
        johnLogin.setId(1L); johnLogin.setUser(john);

        LoginAccess lucasLogin = new LoginAccess("lucas","examplePass");
        User lucas = new User(lucasLogin,"Lucas Master","lucas@email.com");
        lucasLogin.setUser(lucas); lucasLogin.setId(2L);

        List<LoginAccess> loginAccessList = new ArrayList<>();
        loginAccessList.add(johnLogin); //ONLY JOHN IS INCLUDED IN THE REPOSITORY

        //Mocking repository use:
        when(loginRepository.findAll()).thenReturn(loginAccessList);

        //testing method
        boolean nameFound = loginAccessService.checkExistentUsername(johnLogin);
        boolean nameNotFound = loginAccessService.checkExistentUsername(lucasLogin);

        //Asserting results:
        assertFalse(nameNotFound);
        assertTrue(nameFound);

        verify(loginRepository, times(2)).findAll();
        verify(loginRepository, never()).findById(anyLong());

    }

    @Test
    void processLogin() {
        //Creating data:
        LoginAccess johnLogin = new LoginAccess("john","examplePass");
        User john = new User(johnLogin,"John Master","john@email.com");
        johnLogin.setId(1L); johnLogin.setUser(john); john.setId(10L);

        LoginAccess lucasLogin = new LoginAccess("lucas","examplePass");
        User lucas = new User(lucasLogin,"Lucas Master","lucas@email.com");
        lucasLogin.setUser(lucas); lucasLogin.setId(2L); lucas.setId(11L);

        List<LoginAccess> loginAccessList = new ArrayList<>();
        loginAccessList.add(johnLogin); //ONLY JOHN IS INCLUDED IN THE REPOSITORY

        //Mocking repository use:
        when(loginRepository.findAll()).thenReturn(loginAccessList);

        //testing method
        Long johnID = loginAccessService.processLogin(johnLogin);
        Long lucasID = loginAccessService.processLogin(lucasLogin);

        //Asserting results:
        assertNotNull(johnID);
        assertNull(lucasID);

        verify(loginRepository, times(2)).findAll();
        verify(loginRepository, never()).findById(anyLong());
    }
}