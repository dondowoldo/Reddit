package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository users;

    @Autowired
    public UserService(UserRepository users) {
        this.users = users;
    }

    public User save(User user) {
        return users.save(user);
    }

    public Optional<User> findById(Long userId) {
        return users.findById(userId);
    }


}
