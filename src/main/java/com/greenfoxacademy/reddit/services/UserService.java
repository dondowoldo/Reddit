package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Optional;

@Service
public class UserService {
    @Getter
    private User loggedInUser;

    private UserRepository users;

    @Autowired
    public UserService(UserRepository users) {
        this.users = users;
        this.loggedInUser = null;
    }

    public User save(User user) {
        return users.save(user);
    }

    public Optional<User> findById(Long userId) {
        return users.findById(userId);
    }

    public boolean loginUser(String email, String password) {
        Optional<User> stored = getUserByEmail(email);
        if (stored.isPresent() && stored.get().getPassword().equals(password)) {
            loggedInUser = stored.get();
            return true;
        }
        return false;
    }

    public void logOut() {
        loggedInUser = null;
    }

    public boolean loggedIn() {
        return loggedInUser != null;
    }

    public Optional<User> getUserByEmail(String email) {
        return users.findByEmail(email);
    }

}
