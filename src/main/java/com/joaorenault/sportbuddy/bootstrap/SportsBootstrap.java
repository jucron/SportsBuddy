package com.joaorenault.sportbuddy.bootstrap;

import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import com.joaorenault.sportbuddy.services.HashService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SportsBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private HashService hashService;

    public SportsBootstrap(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            userRepository.saveAll(getUsers());
//            matchRepository.saveAll(getMatches());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
//    private List<Match> getMatches() {
//
//    }
    private List<User> getUsers() throws NoSuchAlgorithmException {
        List<User> userList = new ArrayList<>();
//        String examplePass = hashService.hashPass("123");

        User john = new User(1L,"john","examplePass",
                "John Master","john@email.com");
        User larry = new User(2L,"larry","examplePass",
                "Larry Sting","larry1@email.com");

        userList.add(john); userList.add(larry);
        return userList;
    }
}
