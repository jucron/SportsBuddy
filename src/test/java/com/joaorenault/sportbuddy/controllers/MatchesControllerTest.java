package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.services.MatchService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MatchesControllerTest {
    MatchesController matchesController;

    @Mock
    UserService userService;
    @Mock
    MatchService matchService;
    @Mock
    SessionService sessionService;
    @Mock
    Model model;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        matchesController = new MatchesController(matchService,userService,sessionService);
        mockMvc = MockMvcBuilders.standaloneSetup(matchesController).build();
    }
    @Test
    void matchesNotLogged() throws Exception {
        when(sessionService.getSessionUserID()).thenReturn(null);

        //testing
        mockMvc.perform(get("/matches/matches"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));

    }
    @Test
    void matchesLogged() throws Exception {
        User user = new User();
        TreeSet<Match> listOfMatches = new TreeSet<>();
        when(sessionService.getSessionUserID()).thenReturn(1L);
        when(userService.findUserById(1L)).thenReturn(user);
        when(matchService.getMatches()).thenReturn(listOfMatches);

        //testing
        mockMvc.perform(get("/matches/matches"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mainUser"))
                .andExpect(model().attributeExists("matches"))
                .andExpect(view().name("matches/matchesList"));

    }

    @Test
    void createMatchPage() throws Exception {
        User user = new User();
        when(sessionService.getSessionUserID()).thenReturn(1L);
        when(userService.findUserById(1L)).thenReturn(user);

        //testing
        mockMvc.perform(get("/matches/create_match_page"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mainUser"))
                .andExpect(model().attributeExists("match"))
                .andExpect(model().attributeExists("matchCreation"))
                .andExpect(view().name("matches/match_creation"));

    }

    @Test
    void createMatchHasErrors() throws Exception {
        User user = new User();
        when(sessionService.getSessionUserID()).thenReturn(1L);
        when(userService.findUserById(1L)).thenReturn(user);

        //testing
        mockMvc.perform(get("/matches/create_match")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name","")
                                .param("date","")
                                .param("hour","")
                                .param("location","")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mainUser"))
                .andExpect(model().attributeExists("matchCreation"))
                .andExpect(view().name("matches/match_creation"));

    }
    @Test
    void createMatchIsOk() throws Exception {
        User user = new User();
        Match match = new Match();
        user.setId(1L); match.setId(2L);
        when(sessionService.getSessionUserID()).thenReturn(1L);
        when(userService.findUserById(1L)).thenReturn(user);

        //testing
        mockMvc.perform(get("/matches/create_match")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name","AnyName")
                        .param("date","2021-11-21")
                        .param("hour","16:00")
                        .param("location","Somewhere")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:matches"));
        //Assuring match and user are persisted:
        verify(matchService,times(1)).saveMatch(any());
        verify(userService,times(1)).saveUser(any());
        //Asserting user participating matches are associated with the one created:
        Assertions.assertEquals("AnyName",user.getParticipatingMatches().get(0).getName());
    }

    @Test
    void createMatchPageBad() throws Exception {
        User user = new User();
        when(sessionService.getSessionUserID()).thenReturn(1L);
        when(userService.findUserById(1L)).thenReturn(user);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);

        //testing mvc mapping
        mockMvc.perform(get("/matches/match_bad_create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mainUser"))
                .andExpect(model().attributeExists("match"))
                .andExpect(model().attributeExists("matchCreation"))
                .andExpect(view().name("matches/match_creation"));

        //testing feedback attribute
        String viewName = matchesController.createMatchPageBad(model);

        Assertions.assertEquals("matches/match_creation", viewName);
        verify(model, times(1)).addAttribute(eq("matchCreation"),argumentCaptorFeedbackMessage.capture());
        assertTrue(argumentCaptorFeedbackMessage.getValue().isFeedback());
        Assertions.assertEquals(1,argumentCaptorFeedbackMessage.getValue().getPosition());
    }

    @Test
    void matchParticipate() {
    }

    @Test
    void matchLeave() {
    }

    @Test
    void matchDelete() {
    }
}