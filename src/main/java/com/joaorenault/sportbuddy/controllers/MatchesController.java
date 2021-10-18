package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.FeedbackService;
import com.joaorenault.sportbuddy.services.MatchService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.SportsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/matches/")
public class MatchesController {

    private SessionService sessionService = new SessionService();
    private SportsService sportsService = new SportsService();
    private final MatchService matchService;
    private final UserRepository userRepository;

    public MatchesController(MatchService matchService, UserRepository userRepository) {
        this.matchService = matchService;
        this.userRepository = userRepository;
    }

    @GetMapping({"matches","match_delete/matches","match_leave/matches","match_participate/matches"})
    public String matches (Model model) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches",matchService.getMatches());

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
        match.setOwnerName(user.getName());
        match.setSportName(sportSelected);
        matchService.saveMatch(match);

        user.getParticipatingMatches().add(match);
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
    @GetMapping("match_participate/{id}")
    public String matchParticipate (@PathVariable("id") long id) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()+1);
        match.getUsersAttending().add(mainUser);
        mainUser.getParticipatingMatches().add(match);
        matchService.saveMatch(match);
        userRepository.save(mainUser);
        return "redirect:matches";
    }
    @GetMapping("match_leave/{id}")
    public String matchLeave (@PathVariable("id") long id) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()-1);
        match.getUsersAttending().remove(mainUser);
        mainUser.getParticipatingMatches().remove(match);
        matchService.saveMatch(match);
        userRepository.save(mainUser);
        return "redirect:matches";
    }
    @GetMapping("match_delete/{id}")
    public String matchDelete (@PathVariable("id") long id) {
        User mainUser = userRepository.findById(sessionService.getSessionUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id"));
        Match match = matchService.findMatchById(id);
        mainUser.getParticipatingMatches().remove(match);
        matchService.deleteMatchById(match.getId());
        userRepository.save(mainUser);
        return "redirect:matches";
    }
}
