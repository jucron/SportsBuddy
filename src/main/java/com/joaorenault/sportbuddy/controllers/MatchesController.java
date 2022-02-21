package com.joaorenault.sportbuddy.controllers;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.FeedbackMessage;
import com.joaorenault.sportbuddy.services.MatchService;
import com.joaorenault.sportbuddy.services.SessionService;
import com.joaorenault.sportbuddy.services.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MatchesController {

    private final SessionService sessionService;
    private final MatchService matchService;
    private final UserService userService;
    private String matchListFeedback;

    public MatchesController(MatchService matchService, UserService userService,
                             SessionService sessionService) {
        this.matchService = matchService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.matchListFeedback = null;
    }

    @GetMapping({"matches","match_delete/matches","match_leave/matches","match_participate/matches"})
    public String matches (Model model) {
        log.info("matches mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        model.addAttribute("mainUser", mainUser);
        model.addAttribute("matches",matchService.getMatches());
        if (matchListFeedback==null) {
            model.addAttribute("matchInteract", new FeedbackMessage(false));

        } else {
            model.addAttribute("matchInteract", new FeedbackMessage(true,
                    1,matchListFeedback));
        }
        //resetting matchFeedback
        matchListFeedback=null;

        return "matches/matchesList";
    }
    @GetMapping("create_match_page")
    public String createMatchPage (Model model) {
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        log.info("createMatchPage mapping accessed");
        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackMessage(false));
        return "matches/match_creation";
    }
    @GetMapping("create_match")
    public String createMatch (@Valid @ModelAttribute("match") Match match,
                               BindingResult result, Model model) {
        //validate creation of match
        log.info("createMatch mapping accessed");
        if (result.hasErrors()) { //Validating form entry requirements
            User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
//            User mainUser = userService.findUserById(sessionService.getSessionUserID());
            model.addAttribute("mainUser", mainUser);
            model.addAttribute("matchCreation", new FeedbackMessage(
                    true,1,"Please correct all errors."));
            return "matches/match_creation";
        }
        // Associating fields not completed and saving in repository
        User user = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());

        match.setOwnerID(user.getId());
        match.setNumberOfParticipants(1);
        match.getUsersAttending().add(user);
        match.setOwnerName(user.getName());
//        match.setSportName(sportSelected);
        matchService.saveMatch(match);

        user.getParticipatingMatches().add(match);
        userService.saveUser(user);

        //setting matchFeedback:
        matchListFeedback="The match '"+match.getName()+"' was created successfully! Invite all your friends!";

        return "redirect:matches";
    }
    @GetMapping("match_bad_create")
    public String createMatchPageBad (Model model) {
        log.info("createMatchPageBad mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());

        model.addAttribute("mainUser", mainUser);
        model.addAttribute("match", new Match());
        model.addAttribute("matchCreation", new FeedbackMessage(
                true,1,"Must complete all the fields."));
        return "matches/match_creation";
    }
    @GetMapping("match_participate/{id}")
    public String matchParticipate (@PathVariable("id") long id) {
        log.info("matchParticipate mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()+1);
        match.getUsersAttending().add(mainUser);
        mainUser.getParticipatingMatches().add(match);
        matchService.saveMatch(match);
        userService.saveUser(mainUser);

        //Setting match feedback and redirecting to list:
        matchListFeedback="Great! You are now participating in the '"+match.getName()+"' match!";

        return "redirect:matches";
    }
    @GetMapping("match_leave/{id}")
    public String matchLeave (@PathVariable("id") long id) {
        log.info("matchLeave mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        Match match = matchService.findMatchById(id);
        match.setNumberOfParticipants(match.getNumberOfParticipants()-1);
        match.getUsersAttending().remove(mainUser);
        mainUser.getParticipatingMatches().remove(match);
        matchService.saveMatch(match);
        userService.saveUser(mainUser);

        //Setting match feedback and redirecting to list:
        matchListFeedback="Oh no! You just left the '"+match.getName()+"' match!";

        return "redirect:matches";
    }
    @GetMapping ("match_delete/{id}")
    public String matchDelete (@PathVariable("id") long id) {
        log.info("matchDelete mapping accessed");
        User mainUser = userService.findUserByLogin(sessionService.getLoginOfCurrentSession());
        Match match = matchService.findMatchById(id);
        userService.removeAllParticipantsOfAMatch(match);
        matchService.deleteMatchById(match.getId());
        userService.saveUser(mainUser);

        //Setting match feedback and redirecting to list:
        matchListFeedback="The match '"+match.getName()+"', which you were the owner, was deleted!";

        return "redirect:matches";
    }
}
