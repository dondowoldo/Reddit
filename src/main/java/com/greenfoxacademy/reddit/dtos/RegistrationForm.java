package com.greenfoxacademy.reddit.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationForm {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    public RegistrationForm(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegistrationForm() {
    }
}
