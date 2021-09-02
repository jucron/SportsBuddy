package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Login;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.FeedbackService;
import com.joaorenault.sportbuddy.services.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Objects;

@Controller
public class IndexController {

    UserRepository userRepository;
    HashService hashService = new HashService();
    Long mainUserId = null; //temporary session layer


    @Autowired
    public IndexController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Standard index
    @GetMapping({"","/","index"})
    public String startupPage (Model model) {
        model.addAttribute("login", new Login());
        model.addAttribute("account", new FeedbackService(false));
        if (mainUserId!=null){
            return "matches/matches";
        }
        return "index";
    }
    @GetMapping({"logout"})
    public String logout () {
        mainUserId = null;
        return "redirect:index";
    }

    //Bad Login index
    @GetMapping({"index_badlogin"})
    public String startupPageBadLogin (Model model) {
        model.addAttribute("login", new Login());
        model.addAttribute("account", new FeedbackService(
                true,1,"Error logging in, username or password incorrect."));
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
    public String addUser (@Valid User user,
                           BindingResult result) throws NoSuchAlgorithmException {
        if (result.hasErrors() || Objects.equals(user.getUsername(), "") || Objects.equals(user.getPassword(), "")
                || Objects.equals(user.getName(), "") || Objects.equals(user.getEmail(), "")) {
            return "redirect:bad_register_blank";
        }
        //Checking username already registered
        for (User value : userRepository.findAll()) {
            if (Objects.equals(value.getUsername(), user.getUsername())) {
                //username already exists
                return "redirect:bad_register_username";
            }
        }
        String passwordFromUser = (user.getPassword());
        user.setPassword(hashService.hashPass(passwordFromUser));
        userRepository.save(user);
        return "redirect:index_GoodRegister";
    }

    //Login validation
    @PostMapping("login")
    public String login (Login login) throws NoSuchAlgorithmException {
        if (Objects.equals(login.getUsername(), "") || Objects.equals(login.getPassword(), "")) { //Validating form entry requirements (not blank)
            return "redirect:index_badlogin";
        }
        if (login.getUsername()==null) {
            System.out.println("username null");
            return "redirect:index";
        }
        String usernameInput = login.getUsername();
        String passInput = hashService.hashPass(login.getPassword());
        System.out.println("Password from login: "+ passInput);
        //Step 1: validate login
        Iterator<User> users = userRepository.findAll().iterator();
        while (users.hasNext()) {
            if (Objects.equals(users.next().getName(), usernameInput)) {
                //username found
                System.out.println("username found");
                break;
            } else if (!users.hasNext()) { //username not found
                System.out.println("username not found");

                return "redirect:index_badlogin";
            }
        }
        //Step 2: validate password
        for (User value : userRepository.findAll()) {
            if (Objects.equals(value.getPassword(), passInput)) {
                //password found
                System.out.println("pass found");
                mainUserId = value.getId();
                break;
            } else if (!users.hasNext()) { //password not found
                System.out.println("pass NOT found");
                return "redirect:index_badlogin";
            }
        }
        //if all validations passed
        return "redirect:matches";
    }

    @GetMapping("matches")
    public String matches (Model model) {
        User mainUser = userRepository.findById(mainUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        return "matches/matches";
    }



}
