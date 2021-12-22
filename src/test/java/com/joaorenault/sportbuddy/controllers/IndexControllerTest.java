package com.joaorenault.sportbuddy.controllers;

/*
class IndexControllerTest {

    IndexController indexController;
    @Mock
    UserService userService;
    @Mock
    LoginAccessService loginAccessService;
    @Mock
    HashService hashService;
    @Mock
    SessionService sessionService;
    @Mock
    Model model;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        indexController = new IndexController(userService,loginAccessService,hashService,sessionService);
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }
    @Test
    public void testMockMVCRedirectsIndex() throws Exception {
//        when(sessionService.getSessionUserID()).thenReturn(null); //Assuming first access to site

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    @Test
    public void testMockMVCRedirectsMatches() throws Exception {
//        when(sessionService.getSessionUserID()).thenReturn(1L); //Assuming access to index logged

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect: matches/matches"));
    }

    @Test
    void startupPage() {
        //given
        when(sessionService.getSessionUserID()).thenReturn(null); //Assuming first access to site
        ArgumentCaptor<LoginAccess> argumentCaptorLoginAccess = ArgumentCaptor.forClass(LoginAccess.class);
        ArgumentCaptor<FeedbackMessage> argumentCaptorFeedbackMessage = ArgumentCaptor.forClass(FeedbackMessage.class);

        //when
        String viewName = indexController.startupPage(model);

        //then
        Assertions.assertEquals("index", viewName);
        verify(sessionService, times(1)).getSessionUserID();
        verify(model, times(1)).addAttribute(eq("login"),argumentCaptorLoginAccess.capture());
        verify(model, times(1)).addAttribute(eq("account"),argumentCaptorFeedbackMessage.capture());
        assertFalse(argumentCaptorFeedbackMessage.getValue().isFeedback());

    }

    @Test
    void logout() {
        //given

        //when
        String viewName = indexController.logout();

        //then
        Assertions.assertEquals("redirect:index", viewName);
        verify(sessionService, times(1)).setSessionUserID(null);
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
        verify(sessionService, times(0)).getSessionUserID(); //no need to get this object
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
        verify(sessionService, times(0)).getSessionUserID(); //no need to get this object
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

        mockMvc.perform(post("/register")
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

        mockMvc.perform(post("/register")
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

        mockMvc.perform(post("/register")
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


    @Test
    void loginWithErrors() throws Exception {
        //testing
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "")
                        .param("password","")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:index_badlogin"));
    }
    @Test
    void loginDoesNotExists() throws Exception {

        //Assuming Login ID doest not exists
        when(loginAccessService.processLogin(any())).thenReturn(null);
        //testing
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john")
                        .param("password","password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:index_badlogin"));
    }
    @Test
    void loginIsOK() throws Exception {
        //Creating mock data for processing
        User user = new User();
        user.setName("John");

        //Assuming Login exists in repertory
        when(loginAccessService.processLogin(any())).thenReturn(1L);
        //overriding fetching of User in repertory
        when(userService.findUserById(1L)).thenReturn(user);
        //testing:
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john")
                        .param("password","password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:matches/matches"));
    }
}

 */