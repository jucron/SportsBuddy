package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.FeedbackService;
import com.joaorenault.sportbuddy.services.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/matches/")
public class MatchesController {

    UserRepository userRepository;
    MatchRepository matchRepository;
    SessionService sessionUserIDmatch = IndexController.sessionService; //todo

    public MatchesController(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("matches")
    public String matches (Model model) {
        User mainUser = userRepository.findById(sessionUserID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches",matchRepository.findAll());
        return "matches";
    }
    @GetMapping("create_match_page")
    public String createMatchPage (Model model) {
        User mainUser = userRepository.findById(sessionUserID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches", new Match());
        model.addAttribute("account", new FeedbackService(false));
        return "match_creation";
    }
    @PostMapping("create_match")
    public String createMatch (Model model) {
        User mainUser = userRepository.findById(sessionUserID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));
        //todo: validate creation of match
        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches", new Match());
        return "matches";
    }

}
