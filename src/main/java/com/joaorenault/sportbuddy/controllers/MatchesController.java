package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatchesController {

    UserRepository userRepository;
    SessionService sessionService = IndexController.sessionService;

    public MatchesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("matches")
    public String matches (Model model) {
        User mainUser = userRepository.findById(sessionService.sessionUserID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        return "matches/matches";
    }

}
