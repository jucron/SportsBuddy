package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.Match;

import java.util.TreeSet;

public interface MatchService {
    TreeSet<Match> getMatches();
    Match findMatchById(Long l);
    Match saveMatch(Match match);
    void deleteMatchById(Long idToDelete);
}
