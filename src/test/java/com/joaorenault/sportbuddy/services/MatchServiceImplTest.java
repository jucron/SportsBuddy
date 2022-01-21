package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.helper.Sports;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceImplTest {

    MatchService matchService;

    @Mock
    MatchRepository matchRepository;
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        //initialize Mocks
        MockitoAnnotations.initMocks(this); //deprecated
        matchService = new MatchServiceImpl(matchRepository);
    }

    @Test
    void getMatches() {
        //Creating data
        Match match1 = createMatch("Space Basketball", "2021-11-21","16:00","Mars","Bring your own spacesuit!",
                new User(), Sports.BASKETBALL.getSport());
        match1.setId(1L);
        Match match2 = createMatch("Underwater Table Tennis", "2021-12-21","17:00","Pacific Ocean","Bring your own diving suit/gear!",
                new User(), Sports.TABLE_TENNIS.getSport());
        match2.setId(2L);
        TreeSet<Match> matchRepertory = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        matchRepertory.add(match1);  matchRepertory.add(match2);
        //Mocking Repertory use
        when(matchRepository.findAll()).thenReturn(matchRepertory);
        //Testing
        TreeSet<Match> matches = matchService.getMatches();
        //asserting results
        assertNotNull(matches);
        assertEquals(2,matches.size());
        verify(matchRepository, times(1)).findAll();
        assertEquals(match1,matches.first()); assertEquals(match2,matches.last()); //asserting sorting
    }

    @Test
    void findMatchById() {
        //Creating data
        Match match1 = createMatch("Space Basketball", "2021-11-21","16:00","Mars","Bring your own spacesuit!",
                new User(), Sports.BASKETBALL.getSport());
        match1.setId(1L);

        //Mocking Repertory use
        when(matchRepository.findById(match1.getId())).thenReturn(Optional.of(match1));

        //testing
        Match matchToFind = matchService.findMatchById(1L);

        //Assert
        assertNotNull(matchToFind);
        assertEquals(match1,matchToFind);
        verify(matchRepository, times(1)).findById(anyLong());
    }
    @Test
    void findMatchByIdNOTFound() {

        //Mocking repository use:
        when(matchRepository.findById(2L)).thenReturn(Optional.empty());

        //testing
        Exception exception =
                assertThrows(RuntimeException.class, () -> {
                    matchService.findMatchById(2L);
                });
        //asserting
        assertEquals("Match not found", exception.getMessage());

    }
    //helper class:
    private Match createMatch (String name, String date, String hour, String location, String details, User user, String sport) {
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
        match.setSportName(sport);
        user.getParticipatingMatches().add(match);
        return match;
    }
}