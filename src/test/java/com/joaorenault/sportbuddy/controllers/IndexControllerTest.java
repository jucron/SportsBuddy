package com.joaorenault.sportbuddy.controllers;


import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.services.LoginAccessService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IndexControllerTest {

    IndexController indexController;
    @Mock
    UserService userService;
    @Mock
    LoginAccessService loginAccessService;
    @Mock
    SessionService sessionService;
    @Mock
    Model model;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        indexController = new IndexController(userService,loginAccessService,sessionService);
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }
    @Test
    public void testMockMVCRendersIndex() throws Exception {
//        when(sessionService.getSessionUserID()).thenReturn(null); //Assuming first access to site

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void testMockMVCRedirectsMatches() throws Exception {
        LoginAccess login = new LoginAccess();
        User user = new User();
        when(sessionService.getLoginOfCurrentSession()).thenReturn(login);
        when(userService.findUserByLogin(any())).thenReturn(user);

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:matches/matches"));
    }

    @Test
    void startupPage() {
        //given
        when(sessionService.getLoginOfCurrentSession()).thenReturn(null);//Assuming first access to site

        ArgumentCaptor<LoginAccess> argumentCaptorLoginAccess = ArgumentCaptor.forClass(LoginAccess.class);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);

        //when
        String viewName = indexController.startupPage(model);

        //then
        Assertions.assertEquals("index", viewName);
        verify(sessionService, times(1)).getLoginOfCurrentSession();
        verify(model, times(1)).addAttribute(eq("login"),argumentCaptorLoginAccess.capture());
        verify(model, times(1)).addAttribute(eq("account"),argumentCaptorFeedbackMessage.capture());
        assertFalse(argumentCaptorFeedbackMessage.getValue().isFeedback());

    }

    @Test
    void startupPageBadLogin() {
        //given
        ArgumentCaptor<LoginAccess> argumentCaptorLoginAccess = ArgumentCaptor.forClass(LoginAccess.class);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);

        //when
        String viewName = indexController.startupPageBadLogin(model);

        //then
        Assertions.assertEquals("index", viewName);
        verify(sessionService, times(0)).getLoginOfCurrentSession(); //no need to get this object
        verify(model, times(1)).addAttribute(eq("login"),argumentCaptorLoginAccess.capture());
        verify(model, times(1)).addAttribute(eq("account"),argumentCaptorFeedbackMessage.capture());
        assertTrue(argumentCaptorFeedbackMessage.getValue().isFeedback());
        Assertions.assertEquals(1,argumentCaptorFeedbackMessage.getValue().getPosition());
    }

    @Test
    void startupPageGoodLogin() {
        //given
        ArgumentCaptor<LoginAccess> argumentCaptorLoginAccess = ArgumentCaptor.forClass(LoginAccess.class);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);

        //when
        String viewName = indexController.startupPageGoodLogin(model);

        //then
        Assertions.assertEquals("index", viewName);
        verify(sessionService, times(0)).getLoginOfCurrentSession(); //no need to get this object
        verify(model, times(1)).addAttribute(eq("login"),argumentCaptorLoginAccess.capture());
        verify(model, times(1)).addAttribute(eq("account"),argumentCaptorFeedbackMessage.capture());
        assertTrue(argumentCaptorFeedbackMessage.getValue().isFeedback());
        Assertions.assertEquals(2,argumentCaptorFeedbackMessage.getValue().getPosition());
    }

    @Test
    void register() {
        //given
        ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<LoginAccess> argumentCaptorLoginAccess = ArgumentCaptor.forClass(LoginAccess.class);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);
        //when
        String viewName = indexController.register(model);

        //then
        Assertions.assertEquals("registerForm", viewName);
        verify(model, times(1)).addAttribute(eq("user"),argumentCaptorUser.capture());
        verify(model, times(1)).addAttribute(eq("login"),argumentCaptorLoginAccess.capture());
        verify(model, times(1)).addAttribute(eq("account"),argumentCaptorFeedbackMessage.capture());
        assertFalse(argumentCaptorFeedbackMessage.getValue().isFeedback());
    }

    @Test
    void addUserWithGetMethod() throws Exception {
        mockMvc.perform(get("/register")) //GET method not allowed
                .andExpect(status().isMethodNotAllowed());
    }
    @Test
    void addUserWithoutErrors() throws Exception {

        //Creating data:
        //Not necessary.

        //Assuming username is new, for registration:
        when(loginAccessService.checkExistentUsername(any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id","")
                        .param("username", "john")
                        .param("password","examplePass")
                        .param("name","John Master")
                        .param("email","john@email.com")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:index_GoodRegister"));
    }
    @Test
    void addUserUsernameAlreadyExists() throws Exception {
        //Creating data:
        //Not necessary.

        //Assuming username already exists:
        when(loginAccessService.checkExistentUsername(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id","")
                        .param("username", "john")
                        .param("password","examplePass")
                        .param("name","John Master")
                        .param("email","john@email.com")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account")) //feedback message
                .andExpect(view().name("registerForm"));
    }
    @Test
    void addUserFormWithErrors() throws Exception {
        //Creating data:
        //Not necessary.


        //Assuming username is new, for registration:
        when(loginAccessService.checkExistentUsername(any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id","")
                        .param("username", "")
                        .param("password","")
                        .param("name","")
                        .param("email","wrongPassFormat")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account")) //feedback message
                .andExpect(view().name("registerForm"));
    }

}

