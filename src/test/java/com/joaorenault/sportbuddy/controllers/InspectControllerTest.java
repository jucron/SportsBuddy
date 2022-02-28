package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.services.MatchService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class InspectControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private SessionService sessionService;
    @Mock
    private MatchService matchService;

    InspectController inspectController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        inspectController = new InspectController(userService,sessionService,matchService);
        mockMvc = MockMvcBuilders.standaloneSetup(inspectController).build();
    }

    @Test
    void userInspect() throws Exception {
        //given
        long id = 1L;
        LoginAccess login =  new LoginAccess();
        User mainUser = new User();
        User userInspected = new User();

        when(sessionService.getLoginOfCurrentSession()).thenReturn(login);
        when(userService.findUserByLogin(any(LoginAccess.class))).thenReturn(mainUser);
        when(userService.findUserById(id)).thenReturn(userInspected);

        //when
        mockMvc.perform(get("/inspect/"+id+"/user/"))
                //then
                .andExpect(status().isOk())
                .andExpect(view().name("inspect/user_details"));

        verify(userService).findUserByLogin(any(LoginAccess.class));
        verify(sessionService).getLoginOfCurrentSession();
        verify(userService).findUserById(id);
    }

    @Test
    void matchInspect() throws Exception {
        //given
        long id = 1L;
        LoginAccess login =  new LoginAccess();
        User mainUser = new User();
        Match matchInspected = new Match();

        when(sessionService.getLoginOfCurrentSession()).thenReturn(login);
        when(userService.findUserByLogin(any(LoginAccess.class))).thenReturn(mainUser);
        when(matchService.findMatchById(id)).thenReturn(matchInspected);

        //when
        mockMvc.perform(get("/inspect/"+id+"/match/"))
                //then
                .andExpect(status().isOk())
                .andExpect(view().name("inspect/match_details"));

        verify(userService).findUserByLogin(any(LoginAccess.class));
        verify(sessionService).getLoginOfCurrentSession();
        verify(matchService).findMatchById(id);
    }
}