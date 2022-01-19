package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/inspect/")
public class InspectController {

    private final UserService userService;
    private final SessionService sessionService;


    public InspectController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("{id}/user/") // "index"
    public String userInspect (@PathVariable("id") long id, Model model) {
        log.info("userInspect mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        User userInspected = userService.findUserById(id);

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("userInspected", userInspected);

        return "user_details";
    }
}
