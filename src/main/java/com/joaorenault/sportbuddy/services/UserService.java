package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.LoginAccess;
import com.joaorenault.sportbuddy.domain.Match;
import com.joaorenault.sportbuddy.domain.User;

import java.util.TreeSet;

public interface UserService {
    TreeSet<User> getUsers();
    User findUserById(Long l);
    User findUserByEmail(String email);
    User saveUser(User user);
    void deleteUserById(Long idToDelete);
    User findUserByLogin(LoginAccess login);
    void removeAllParticipantsOfAMatch(Match match);

    boolean checkExistentEmail(User user);
}
