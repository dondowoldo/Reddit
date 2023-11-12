package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.dtos.RegistrationForm;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Getter
    private User loggedInUser;

    private UserRepository users;
    private UserValidation userValidation;

    @Autowired
    public UserService(UserRepository users, UserValidation userValidation) {
        this.users = users;
        this.loggedInUser = null;
        this.userValidation = userValidation;
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

    public String registerUser(RegistrationForm form) {
        try {
            userValidation.registrationValid(form);
            User registeredUser = mapFormToUser(form);
            users.save(registeredUser);
            loggedInUser = registeredUser;
        } catch (IllegalArgumentException | EntityExistsException e) {
            return e.getMessage();
        }
        return "valid";
    }


    //Use only after validation (userValidation.registrationValid)
    public User mapFormToUser(RegistrationForm form) {
        return new User(form.getName(), form.getEmail(), form.getPassword());
    }

}
