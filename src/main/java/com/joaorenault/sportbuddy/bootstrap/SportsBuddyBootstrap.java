package com.joaorenault.sportbuddy.bootstrap;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.Sports;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Component
public class SportsBuddyBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public SportsBuddyBootstrap(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        userRepository.saveAll(getUsers());

        matchRepository.save(getMatch1(getUserByUsername("eric")));
        matchRepository.save(getMatch2(getUserByUsername("larissa")));

    }
    private List<User> getUsers () {
        List<User> users = new ArrayList<>();

        User eric = new User("eric","123","Eric Floyd", "eric@hotmail.com",new Sports());
        eric.getSports().setBASEBALL(true);
        eric.getSports().setSOCCER(true);
        users.add(eric);
        User john = new User("john","321","John Master", "johnny@email.com",new Sports());
        eric.getSports().setSOCCER(true);
        users.add(john);
        User larissa = new User("larissa","larissa","Larissa Oliveira", "lary@email.com",new Sports());
        eric.getSports().setTABLE_TENNIS(true);
        eric.getSports().setBASEBALL(true);
        users.add(larissa);

        return users;
    }

    private Match getMatch1 (Long ownerId) {

        Match match = new Match("quick 3 on 3","17 october","15h","Main square",
                "Please someone bring the ball?", ownerId, new Sports());
        match.getSports().setSOCCER(true);
        return match;
    }
    private Match getMatch2 (Long ownerId) {

        Match match = new Match("Table-Tennis tournament","10 october","10h","Central Park",
                "I'll message everyone one day before just to confirm.", ownerId, new Sports());
        match.getSports().setTABLE_TENNIS(true);
        return match;
    }
    private Long getUserByUsername (String username) {
        for (User value: userRepository.findAll()) {
            if (Objects.equals(value.getUsername(), username)) {
                return value.getId();
            }
        }
        return null;
    }
}
