package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.services.LoginAccessService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Controller
public class IndexController {

    private final UserService userService;
    private final LoginAccessService loginAccessService;
    private final SessionService sessionService;

    public IndexController(UserService userService, LoginAccessService loginAccessService,
                           SessionService sessionService) {
        this.userService = userService;
        this.loginAccessService = loginAccessService;
        this.sessionService = sessionService;
    }

    //Standard index
    @GetMapping({"","/"}) // "index"
    public String startupPage (Model model) {
        log.info("index mapping accessed");
        if (sessionService.getLoginOfCurrentSession()!=null) {
            log.info("User is already logged in, redirecting to matches list");
            return "redirect:matches/matches";
        }
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackMessage(false));

        return "index";
    }

    //Bad Login index
    @GetMapping({"index_badlogin"})
    public String startupPageBadLogin (Model model) {
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackMessage(
                true,1,"Username and password does not match or do not exist."));
        log.info("index_badlogin mapping accessed");
        return "index";
    }
    //Good registration Index
    @GetMapping({"index_GoodRegister"})
    public String startupPageGoodLogin(Model model) {
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackMessage(
                true,2,"Account successful created!"));
        log.info("index_GoodRegister mapping accessed");
        return "index";
    }
    // Register page:
    @GetMapping("register_page")
    public String register (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("login", new LoginAccess());
        model.addAttribute("account", new FeedbackMessage(false));
        log.info("register_page mapping accessed");
        return "registerForm";
    }

    // Process register input
    @PostMapping("register")
    public String addUser (@Valid @ModelAttribute("user") User user, BindingResult resultUser, @Valid @ModelAttribute("login") LoginAccess login,
                           BindingResult resultLogin, Model model) throws NoSuchAlgorithmException {
        log.info("register mapping accessed");
        if (resultUser.hasErrors() || resultLogin.hasErrors()) {
                model.addAttribute("account", new FeedbackMessage(
                        true,1,"Please correct errors."));
                resultLogin.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
                resultUser.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return "registerForm";
        }
        //Checking username already registered
        if (loginAccessService.checkExistentUsername(login)) {
            //username already exists
            model.addAttribute("account", new FeedbackMessage(
                    true,1,"Username is already taken."));
            log.debug("Username already taken");
            return "registerForm";
        }
        //Checking email already registered
        if (userService.checkExistentEmail(user)) {
            //Email already exists
            model.addAttribute("account", new FeedbackMessage(
                    true,1,"Email address is already taken."));
            log.debug("Email already taken");
            return "registerForm";
        }
        //if username and email are not existent in DB, proceeds to persist data
        login.setPassword(login.getPassword());
        loginAccessService.encodePassAndSaveLogin(login); //Login has ID now
        user.setLogin(login);
        userService.saveUser(user); //User has ID now
        login.setUser(user);
        loginAccessService.saveLogin(login); //Persisting login with the user ID associated
        return "redirect:index_GoodRegister";
    }
    @GetMapping("updateAccount")
    public String updateAccount (Model model) {
        log.info("updateAccount mapping accessed");
        LoginAccess currentLogin = sessionService.getLoginOfCurrentSession();
        if (currentLogin==null) {
            log.info("User is NOT logged in, redirecting to index");
            return "redirect:index";
        }
        log.info("User is logged and attempted to update account details");

        model.addAttribute("user", userService.findUserByLogin(currentLogin));
        model.addAttribute("login", currentLogin);
        model.addAttribute("updateForm", new UpdateForm());
        model.addAttribute("account", new FeedbackMessage(true,3,"Update your account"));
        return "updateForm";
    }
    @PostMapping("update")
    public String update (@Valid @ModelAttribute("updateForm") UpdateForm updateForm, BindingResult result, Model model) throws NoSuchAlgorithmException {
        log.info("register mapping accessed");
        if (result.hasErrors()) {
            log.info("Update Form has errors");
            model.addAttribute("account", new FeedbackMessage(
                    true,1,"Please correct errors."));
            result.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            LoginAccess currentLogin = sessionService.getLoginOfCurrentSession();
            model.addAttribute("user", userService.findUserByLogin(currentLogin));
            model.addAttribute("login", currentLogin);
            model.addAttribute("updateForm", new UpdateForm());
            return "updateForm";
        }
        // proceeds to persist data:
        //Fetching login and user objects
        LoginAccess currentLogin = sessionService.getLoginOfCurrentSession();
        User currentUser = userService.findUserByLogin(currentLogin);
        //Setting new values
        currentLogin.setPassword(updateForm.getPassword());
        currentUser.setName(updateForm.getName());
        //Persisting new data
        loginAccessService.encodePassAndSaveLogin(currentLogin);
        userService.saveUser(currentUser);
        return "redirect:/";
    }
}
class UpdateForm {
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Name is mandatory")
    private String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
