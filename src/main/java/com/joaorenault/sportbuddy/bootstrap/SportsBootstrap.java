package com.joaorenault.sportbuddy.bootstrap;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.SportsService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SportsBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final SportsService sportsService = new SportsService();

    public SportsBootstrap(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // Adding users:
        User john = new User(1L,"john","examplePass",
                "John Master","john@email.com");
        userRepository.save(john);
        User larry = new User(2L,"larry","examplePass",
                "Larry Sting","larry1@email.com");
        userRepository.save(larry);
        User larissa = new User(3L,"larry","examplePass",
                "Larissa Hearth","larrissa@email.com");
        userRepository.save(larissa);
        User bruna = new User(4L,"bruna","examplePass",
                "Bruna Lulu","bruna@email.com");
        userRepository.save(bruna);

        //Adding matches and relating users:
        createMatch("3 on 3", "20/11/21","16hrs","Plaza sports center","Bring all friends!",
                john, 2);
        createMatch("Soccer relax", "12/11/21","10hrs","Main field","I have ball already",
                larry, 1);
        createMatch("Tennis with friends", "13/11/21","15hrs","Tennis club","Everyone is welcome!",
                larissa,4);
        createMatch("Beach Volleyball!", "25/11/21", "11hrs","Long Beach", "We're starting when 4 is already there",
                bruna,3);
        // Making people participate:
        participateMatch(1L, 6L);
        participateMatch(2L, 7L);
        participateMatch(4L, 7L);

    }

    private void createMatch (String name, String date, String hour,String location, String details,User user, int sport) {
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
        matchRepository.save(match);
        user.getParticipatingMatches().add(match);
        userRepository.save(user);
    }

    private void participateMatch(Long userId,Long matchId) {
        Match match = matchRepository.findById(matchId).get();
        User user = userRepository.findById(userId).get();

        user.getParticipatingMatches().add(match);
        match.setNumberOfParticipants(match.getNumberOfParticipants()+1);
        match.getUsersAttending().add(user);
        matchRepository.save(match);
        userRepository.save(user);

    }
}
