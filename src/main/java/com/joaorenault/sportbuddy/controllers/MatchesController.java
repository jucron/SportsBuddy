package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.FeedbackService;
import com.joaorenault.sportbuddy.services.FindServiceImpl;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.SportsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/matches/")
public class MatchesController {

    private UserRepository userRepository;
    private MatchRepository matchRepository;
    private FindServiceImpl userService = new FindServiceImpl();
    private SessionService sessionService = new SessionService();
    private SportsService sportsService = new SportsService();

    public MatchesController(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("matches")
    public String matches (Model model) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches",matchRepository.findAll());
        return "matches/matches";
    }
    @GetMapping("create_match_page")
    public String createMatchPage (Model model) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackService(false));
        return "matches/match_creation";
    }
    @GetMapping("create_match")
    public String createMatch (Match match) {

        //validate creation of match
        if (Objects.equals(match.getName(), "") ||
                Objects.equals(match.getDate(), "") ||
                Objects.equals(match.getHour(), "") ||
                Objects.equals(match.getDetails(), "")) { //Validating form entry requirements (not blank)
            return "redirect:match_bad_create";
        }
        // Associating fields not completed and saving in repository
        Long userID = sessionService.getSessionUserID();
        String sportSelected = sportsService.sportSelected(match.getSportChoice());
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + userID));

        match.setOwnerID(userID);
        match.setNumberOfParticipants(1);
        match.getUsersAttending().add(user);
        user.getParticipatingMatches().add(match);
        match.setOwnerName(user.getName());
        match.setSportName(sportSelected);
        matchRepository.save(match);
        userRepository.save(user);
        return "redirect:matches";
    }
    @GetMapping("match_bad_create")
    public String createMatchPageBad (Model model) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackService(
                true,1,"Must complete all the fields."));
        return "matches/match_creation";
    }
}
