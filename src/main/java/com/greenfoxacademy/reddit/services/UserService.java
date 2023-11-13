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
    private static User currentUser;
    private DbService dbService;
    private UserValidation userValidation;

    @Autowired
    public UserService(UserValidation userValidation, DbService dbService) {
        currentUser = null;
        this.userValidation = userValidation;
        this.dbService = dbService;
    }

    public boolean loginUser(String email, String password) {
        String hashedPass;
        try {
            hashedPass = encryptPassword(password);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

        Optional<User> stored = getUserByEmail(email);
        if (stored.isPresent() && stored.get().getPassword().equals(hashedPass)) {
            currentUser = stored.get();
            return true;
        }
        return false;
    }

    public void logOut() {
        currentUser = null;
    }

    public boolean loggedIn() {
        return currentUser != null;
    }

    public Optional<User> getUserByEmail(String email) {
        return dbService.getUsers().findByEmail(email);
    }

    public String registerUser(RegistrationForm form) {
        try {
            userValidation.registrationValid(form);
            User registeredUser = mapFormToUser(form);
            registeredUser.setPassword(encryptPassword(registeredUser.getPassword()));
            dbService.getUsers().save(registeredUser);
            currentUser = registeredUser;
        } catch (IllegalArgumentException | EntityExistsException | NoSuchAlgorithmException e) {
            return e.getMessage();
        }
        return "valid";
    }

    //Use only after validation (userValidation.registrationValid)
    public User mapFormToUser(RegistrationForm form) {
        return new User(form.getName(), form.getEmail(), form.getPassword());
    }

    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString();
    }

}
