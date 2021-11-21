package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.helper.SportsChoice;
import com.joaorenault.sportbuddy.services.MatchService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/matches/")
public class MatchesController {

    private SessionService sessionService;
    private SportsChoice sportsService = new SportsChoice();
    private final MatchService matchService;
    private final UserService userService;

    public MatchesController(MatchService matchService, UserService userService,
                             SessionService sessionService) {
        this.matchService = matchService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping({"matches","match_delete/matches","match_leave/matches","match_participate/matches"})
    public String matches (Model model) {
        //Go back to index in case of not logged in
        if (sessionService.getSessionUserID()==null) {
            return "redirect:/index";
        }
        User mainUser = userService.findUserById(sessionService.getSessionUserID());
        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches",matchService.getMatches());

        return "matches/matchesList";
    }
    @GetMapping("create_match_page")
    public String createMatchPage (Model model) {
        User mainUser = userService.findUserById(sessionService.getSessionUserID());

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackMessage(false));
        return "matches/match_creation";
    }
    @GetMapping("create_match")
    public String createMatch (@Valid @ModelAttribute("match") Match match,
                               BindingResult result, Model model) {
        //validate creation of match
        if (result.hasErrors()) { //Validating form entry requirements
            User mainUser = userService.findUserById(sessionService.getSessionUserID());
            model.addAttribute("mainUser", mainUser);
            model.addAttribute("matchCreation", new FeedbackMessage(
                    true,1,"Please correct all errors."));
            return "matches/match_creation";
        }
        // Associating fields not completed and saving in repository
        Long userID = sessionService.getSessionUserID();
        String sportSelected = sportsService.sportSelected(match.getSportChoice());
        User user = userService.findUserById(userID);

        match.setOwnerID(userID);
        match.setNumberOfParticipants(1);
        match.getUsersAttending().add(user);
        match.setOwnerName(user.getName());
        match.setSportName(sportSelected);
        matchService.saveMatch(match);

        user.getParticipatingMatches().add(match);
        userService.saveUser(user);
        return "redirect:matches";
    }
    @GetMapping("match_bad_create")
    public String createMatchPageBad (Model model) {
        User mainUser = userService.findUserById(sessionService.getSessionUserID());

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackMessage(
                true,1,"Must complete all the fields."));
        return "matches/match_creation";
    }
    @GetMapping("match_participate/{id}")
    public String matchParticipate (@PathVariable("id") long id) {
        User mainUser = userService.findUserById(sessionService.getSessionUserID());
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()+1);
        match.getUsersAttending().add(mainUser);
        mainUser.getParticipatingMatches().add(match);
        matchService.saveMatch(match);
        userService.saveUser(mainUser);
        return "redirect:matches";
    }
    @GetMapping("match_leave/{id}")
    public String matchLeave (@PathVariable("id") long id) {
        User mainUser = userService.findUserById(sessionService.getSessionUserID());
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()-1);
        match.getUsersAttending().remove(mainUser);
        mainUser.getParticipatingMatches().remove(match);
        matchService.saveMatch(match);
        userService.saveUser(mainUser);
        return "redirect:matches";
    }
    @GetMapping("match_delete/{id}")
    public String matchDelete (@PathVariable("id") long id) {
        User mainUser = userService.findUserById(sessionService.getSessionUserID());
        Match match = matchService.findMatchById(id);
        mainUser.getParticipatingMatches().remove(match);
        matchService.deleteMatchById(match.getId());
        userService.saveUser(mainUser);
        return "redirect:matches";
    }
}
