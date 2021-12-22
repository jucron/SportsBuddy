package com.joaorenault.sportbuddy.repositories;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends CrudRepository<LoginAccess, Long> {
    LoginAccess findByUsername (String username);
}
