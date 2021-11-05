package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.SportsChoice;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MatchServiceIT {
    @Autowired
    MatchService matchService;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    MatchRepository matchRepository;

    SportsChoice sportsService = new SportsChoice();

    @Test
    @Transactional
    void saveMatch() {
        //Creating data:
        Match match1 = createMatch("Space Basketball", "2021-11-21","16:00","Mars","Bring your own spacesuit!",
                new User(), 2);

        //testing method:
        Match savedMatch = matchService.saveMatch(match1);

        //Asserting results:
        TreeSet<Match> matches = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        matchRepository.findAll().iterator().forEachRemaining(matches::add);

        assertNotNull(savedMatch);
        assertEquals("Space Basketball",savedMatch.getName());
        assertEquals("Mars",savedMatch.getLocation());
        assertEquals((4+1),matches.size()); //Bootstrap (4) + newMatch (1)
        assertTrue(matches.iterator().hasNext());
    }

    @Test
    @Transactional
    void deleteMatchById() {
        //Creating data:
        Match match1 = createMatch("Space Basketball", "2021-11-21","16:00","Mars","Bring your own spacesuit!",
                new User(), 2);
        matchRepository.save(match1);
        long matchRepositorySizeBefore = matchRepository.count();

        //testing method:
        matchService.deleteMatchById(match1.getId());

        //Asserting results:
        TreeSet<Match> matches = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        matchRepository.findAll().iterator().forEachRemaining(matches::add);

        assertNotNull(matches);
        assertNotEquals(matchRepositorySizeBefore,matches.size());
        assertEquals((4+1-1),matches.size()); //Bootstrap (4) + newUser (1) - newUser (1)
        assertFalse(matches.contains(match1));
    }
    //helper class:
    private Match createMatch (String name, String date, String hour, String location, String details, User user, int sport) {
        Match match = new Match();
        match.setName(name);
        match.setDate(date);
        match.setHour(hour);
        match.setLocation(location);
        match.setDetails(details);
        match.setOwnerID(user.getId());
        match.setNumberOfParticipants(1);
        match.getUsersAttending().add(user);
        match.setOwnerName(user.getName());
        match.setSportName(sportsService.sportSelected(sport));
        user.getParticipatingMatches().add(match);
        return match;
    }
}
