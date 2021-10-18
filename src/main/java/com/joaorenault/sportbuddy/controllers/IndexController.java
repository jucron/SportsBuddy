package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Login;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.services.FeedbackService;
import com.joaorenault.sportbuddy.services.HashService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Controller
public class IndexController {

    private final UserService userService;
    private HashService hashService = new HashService();
    private SessionService sessionService = new SessionService(null);

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    //Standard index
    @GetMapping({"","/","index"})
    public String startupPage (Model model) {
        model.addAttribute("login", new Login());
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
        model.addAttribute("login", new Login());
        model.addAttribute("account", new FeedbackService(
                true,1,"Error, username and password are incorrect or do not exist."));
        return "index";
    }
    //Good registration Index
    @GetMapping({"index_GoodRegister"})
    public String startupPageBadPass (Model model) {
        model.addAttribute("login", new Login());
        model.addAttribute("account", new FeedbackService(
                true,2,"Account successful created!"));
        return "index";
    }
    // Register page:
    @GetMapping("register_page")
    public String register (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("account", new FeedbackService(false));
        return "register";
    }
    @GetMapping("bad_register_blank")
    public String badRegister (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("account", new FeedbackService(
                true,1,"Must fill all the fields."));
        return "register";
    }
    @GetMapping("bad_register_username")
    public String badRegisterUsername (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("account", new FeedbackService(
                true,1,"Username already taken, please choose another."));
        return "register";
    }
    // Process register input
    @PostMapping("register")
    public String addUser (User user,
                           BindingResult result) throws NoSuchAlgorithmException {
        if (result.hasErrors() || Objects.equals(user.getUsername(), "") || Objects.equals(user.getPassword(), "")
                || Objects.equals(user.getName(), "") || Objects.equals(user.getEmail(), "")) {
            return "redirect:bad_register_blank";
        }
        //Checking username already registered

        for (User value : userService.getUsers()) {
            if (Objects.equals(value.getUsername(), user.getUsername())) {
                //username already exists
                return "redirect:bad_register_username";
            }
        }
        user.setPassword(hashService.hashPass(user.getPassword()));
        userService.saveUser(user);
        return "redirect:index_GoodRegister";
    }

    //Login validation
    @PostMapping("login")
    public String login (Login login) throws NoSuchAlgorithmException {
        if (Objects.equals(login.getUsername(), "")
                || Objects.equals(login.getPassword(), "")
                || (login.getUsername() == null)) { //Validating form entry requirements (not blank)
            return "redirect:index_badlogin";
        }
        String usernameInput = login.getUsername();
//        System.out.println("username from login: " + usernameInput);
        String passInput = hashService.hashPass(login.getPassword());
//        System.out.println("Password from login: " + passInput);
        //Step 1: validate login
        for (User userLogin : userService.getUsers()) {
            if (Objects.equals(userLogin.getUsername(), usernameInput)) {
                //username found
//                System.out.println("username found in DB");
                //Step 2: validate password
                for (User userPass : userService.getUsers()) {
                    if (Objects.equals(userPass.getPassword(), passInput)) {
//                        System.out.println("Password found in DB");
                        sessionService.setSessionUserID(userLogin.getId());
                        sessionService.setSessionUserName(userLogin.getName());
                        return "redirect:matches/matches";
                    }
                }
            }
        }
//        System.out.println("Username or password failed");
        return "redirect:index_badlogin";
    }
}
