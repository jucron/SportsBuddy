package com.joaorenault.sportbuddy.bootstrap;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.SportsChoice;
import com.joaorenault.sportbuddy.repositories.LoginRepository;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SportsBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final SportsChoice sportsService = new SportsChoice();
    private final PasswordEncoder passwordEncoder;

    public SportsBootstrap(MatchRepository matchRepository, UserRepository userRepository, LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String examplePass = passwordEncoder.encode("examplePass");
        // Adding users:
        LoginAccess johnLogin = new LoginAccess("john",examplePass);
        loginRepository.save(johnLogin);
        User john = new User(johnLogin,"John Master","john@email.com");
        johnLogin.setUser(john);
        loginRepository.save(johnLogin);
        userRepository.save(john);

        LoginAccess larryLogin = new LoginAccess("larry",examplePass);
        loginRepository.save(larryLogin);
        User larry = new User(larryLogin,"Larry Sting","larry1@email.com");
        larryLogin.setUser(larry);
        loginRepository.save(larryLogin);
        userRepository.save(larry);

        LoginAccess larissaLogin = new LoginAccess("larissa",examplePass);
        loginRepository.save(larissaLogin);
        User larissa = new User(larissaLogin,"Larissa Hearth","larrissa@email.com");
        larissaLogin.setUser(larissa);
        loginRepository.save(larissaLogin);
        userRepository.save(larissa);

        LoginAccess brunaLogin = new LoginAccess("bruna",examplePass);
        loginRepository.save(brunaLogin);
        User bruna = new User(brunaLogin,"Bruna Lulu","bruna@email.com");
        brunaLogin.setUser(bruna);
        loginRepository.save(brunaLogin);
        userRepository.save(bruna);

        //Adding matches and relating users:
        Match match1 = createMatch("3 on 3", "2021-11-21","16:00","Plaza sports center","Bring all friends!",
                john, 2);
        Match match2 = createMatch("Soccer relax", "2021-11-10","10:00","Main field","I have ball already",
                larry, 1);
        Match match3 = createMatch("Tennis with friends", "2021-11-13","15:00","Tennis club","Everyone is welcome!",
                larissa,4);
        Match match4 = createMatch("Beach Volleyball!", "2021-11-25", "11:00","Long Beach", "We're starting when 4 is already there",
                bruna,3);
        // Making people participate:
        participateMatch(john.getId(), match1.getId());
        participateMatch(larry.getId(), match2.getId());
        participateMatch(bruna.getId(), match2.getId());

    }

    private Match createMatch (String name, String date, String hour,String location, String details,User user, int sport) {
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
        return match;
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
