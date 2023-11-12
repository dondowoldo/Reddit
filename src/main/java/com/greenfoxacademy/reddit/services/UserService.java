package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.dtos.RegistrationForm;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {
    @Getter
    private static User CURRENT_USER;

    private UserRepository users;
    private UserValidation userValidation;

    @Autowired
    public UserService(UserRepository users, UserValidation userValidation) {
        CURRENT_USER = null;
        this.users = users;
        this.userValidation = userValidation;
    }

    public User save(User user) {
        return users.save(user);
    }

    public Optional<User> findById(Long userId) {
        return users.findById(userId);
    }

    public boolean loginUser(String email, String password) {
        String hashedpass;
        try {
            hashedpass = encryptPassword(password);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        Optional<User> stored = getUserByEmail(email);
        if (stored.isPresent() && stored.get().getPassword().equals(hashedpass)) {
            CURRENT_USER = stored.get();
            return true;
        }
        return false;
    }

    public void logOut() {
        CURRENT_USER = null;
    }

    public boolean loggedIn() {
        return CURRENT_USER != null;
    }

    public Optional<User> getUserByEmail(String email) {
        return users.findByEmail(email);
    }

    public String registerUser(RegistrationForm form) {
        try {
            userValidation.registrationValid(form);
            User registeredUser = mapFormToUser(form);
            users.save(registeredUser);
            CURRENT_USER = registeredUser;
        } catch (IllegalArgumentException | EntityExistsException e) {
            return e.getMessage();
        }
        return "valid";
    }


    //Use only after validation (userValidation.registrationValid)
    public User mapFormToUser(RegistrationForm form) {
        String password;
        try {
            password = encryptPassword(form.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error. Try to register later");
        }
        return new User(
                form.getName(),
                form.getEmail(),
                password
        );
    }

    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString();
    }

}
