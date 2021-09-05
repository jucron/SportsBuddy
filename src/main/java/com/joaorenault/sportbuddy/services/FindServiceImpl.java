package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.User;

import java.util.Objects;

public class FindServiceImpl {

    public FindServiceImpl() {
    }

    public User findUserById(Long id, Iterable<User> repository) {
        for (User user: repository) {
            if (Objects.equals(user.getId(), id)) {
                return user;
            }
        }
        return null;
    }
}
