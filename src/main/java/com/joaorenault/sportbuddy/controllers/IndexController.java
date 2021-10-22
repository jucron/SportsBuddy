package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Controller
public class IndexController {

    private final UserService userService;
    private final LoginAccessService loginAccessService;

    private HashService hashService = new HashService();
    private SessionService sessionService = new SessionService(null);

    public IndexController(UserService userService, LoginAccessService loginAccessService) {
        this.userService = userService;
        this.loginAccessService = loginAccessService;
    }

    //Standard index
    @GetMapping({"","/","index"})
    public String startupPage (Model model) {
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackService(false));
        if (sessionService.getSessionUserID()!=null){
            return "matches/matches";
        }
        return "index";
    }
    @GetMapping({"logout"})
    public String logout () {
        sessionService.setSessionUserID(null);
        return "redirect:index";
    }
    //Bad Login index
    @GetMapping({"index_badlogin"})
    public String startupPageBadLogin (Model model) {
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackService(
                true,1,"Username and password does not match or do not exist."));
        return "index";
    }
    //Good registration Index
    @GetMapping({"index_GoodRegister"})
    public String startupPageBadPass (Model model) {
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackService(
                true,2,"Account successful created!"));
        return "index";
    }
    // Register page:
    @GetMapping("register_page")
    public String register (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackService(false));
        return "register";
    }

    // Process register input
    @PostMapping("register")
    public String addUser (@Valid @ModelAttribute("user") User user, BindingResult resultUser, @Valid @ModelAttribute("login") LoginAccess login,
                           BindingResult resultLogin, Model model) throws NoSuchAlgorithmException {
        if (resultUser.hasErrors() || resultLogin.hasErrors()) {
                model.addAttribute("account", new FeedbackService(
                        true,1,"Please correct errors."));
                resultLogin.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
                resultUser.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return "register";
        }
        //Checking username already registered
        if (loginAccessService.checkExistentUsername(login)) {
            //username already exists
            model.addAttribute("account", new FeedbackService(
                    true,1,"Username is already taken."));
            log.debug("Username already taken");
            return "register";
        }
        //if not existent, proceeds to persist data
        login.setPassword(hashService.hashPass(login.getPassword()));
        loginAccessService.saveLogin(login); //Login has ID now
        user.setLogin(login);
        userService.saveUser(user); //User has ID now
        login.setUser(user);
        loginAccessService.saveLogin(login); //Persisting login with the user ID associated
        return "redirect:index_GoodRegister";
    }
    //Login validation
    @PostMapping("login")
    public String login (@Valid @ModelAttribute("login") LoginAccess login,
                         BindingResult result) throws NoSuchAlgorithmException {
        if (result.hasErrors()) { //Validating form entry requirements
            return "redirect:index_badlogin";
        }
        login.setPassword(hashService.hashPass(login.getPassword()));

        //Validating login
        Long userID = loginAccessService.processLogin(login);
        if (!(userID == null)) {
            User user = userService.findUserById(userID);
            sessionService.setSessionUserID(userID);
            sessionService.setSessionUserName(user.getName());
            return "redirect:matches/matches";
        }
        //If bad-login
        return "redirect:index_badlogin";
    }
}
