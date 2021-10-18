package com.joaorenault.sportbuddy.services;

import com.joaorenault.sportbuddy.domain.User;
import com.joaorenault.sportbuddy.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public TreeSet<User> getUsers() {
        TreeSet<User> users = new TreeSet<>(Comparator.comparingInt(o -> (o.getId().intValue())));
        userRepository.findAll().iterator().forEachRemaining(users::add);
        return users;
    }

    @Override
    public User findUserById(Long l) {
        Optional<User> userOptional = userRepository.findById(l);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        return userOptional.get();
    }

    @Override
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public void deleteUserById(Long idToDelete) {
        userRepository.deleteById(idToDelete);
    }
}