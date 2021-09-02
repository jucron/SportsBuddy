package com.joaorenault.sportbuddy.repositories;

import com.joaorenault.sportbuddy.domain.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
}
