package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public void registrationValid(User user) throws IllegalArgumentException, EntityExistsException {

        // Email validation
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(user.getEmail());
        if (user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("You need to enter an e-mail");
        }
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid e-mail format.");
        }
        // Password validation
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password needs to be at least "
                    + MIN_PASSWORD_LENGTH + " characters long.");
        }
    }


}
