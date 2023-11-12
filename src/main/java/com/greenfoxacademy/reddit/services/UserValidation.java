package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.dtos.RegistrationForm;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Component
public class UserValidation {
    private UserRepository userRepository;
    private final String EMAIL_PATTERN ="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private final int MIN_PASSWORD_LENGTH = 8;


    @Autowired
    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void registrationValid(RegistrationForm form) throws IllegalArgumentException, EntityExistsException {
        // Name validation
        if (form.getName().isEmpty()) {
            throw new IllegalArgumentException("You need to enter your name");
        }

        // Email validation
        if (getUserByEmail(form.getEmail()).isPresent()) {
            throw new EntityExistsException("This e-mail already exists.");
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(form.getEmail());
        if (form.getEmail().isEmpty()) {
            throw new IllegalArgumentException("You need to enter an e-mail");
        }
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid e-mail format.");
        }

        // Password validation
        if (form.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password needs to be at least "
                    + MIN_PASSWORD_LENGTH + " characters long.");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
}
