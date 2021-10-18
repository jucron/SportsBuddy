package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.User;

import java.util.TreeSet;

public interface UserService {
    TreeSet<User> getUsers();
    User findUserById(Long l);
    User saveUser(User user);
    void deleteUserById(Long idToDelete);
}
