package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public TreeSet<Match> getMatches() {
        TreeSet<Match> matches = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        matchRepository.findAll().iterator().forEachRemaining(matches::add);
        return matches;
    }

    @Override
    public Match findMatchById(Long l) {
        Optional<Match> matchOptional = matchRepository.findById(l);

        if (!matchOptional.isPresent()) {
            throw new RuntimeException("Match not found");
        }

        return matchOptional.get();
    }

    @Override
    public Match saveMatch(Match match) {
        Match savedMatch = matchRepository.save(match);

        return savedMatch;
    }

    @Override
    public void deleteMatchById(Long idToDelete) {
        matchRepository.deleteById(idToDelete);
    }
}
