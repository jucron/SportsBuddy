package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.services.LoginAccessService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerIT {
    @Autowired
    LoginAccessService loginAccessService;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserService userService;

    IndexController indexController;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    private final String EXISTING_USERNAME = "john"; // given by Bootstrap start data
    private final String EXISTING_PASSWORD = "examplePass"; // given by Bootstrap start data

    @BeforeEach
    void setUp() {
        indexController = new IndexController(userService,loginAccessService,sessionService);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void goodLogin() throws Exception {

        //when
        mockMvc.perform(post("/login").secure(true)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username",EXISTING_USERNAME)
                        .param("password",EXISTING_PASSWORD)
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/matches/matches"));
    }
    @Test
    void badLoginWrongData() throws Exception {

        //when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","doNotExist")
                        .param("password","doNotExist")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/index_badlogin"));
    }
    @Test
    void badLoginWrongUsername() throws Exception {

        //when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","doNotExist")
                        .param("password",EXISTING_PASSWORD)
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/index_badlogin"));
    }
    @Test
    void badLoginWrongPassword() throws Exception {

        //when
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username",EXISTING_USERNAME)
                        .param("password","doNotExist")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/index_badlogin"));
    }
    @Test
    void logout() throws Exception {
        //when
        mockMvc.perform(post("/logout")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}
