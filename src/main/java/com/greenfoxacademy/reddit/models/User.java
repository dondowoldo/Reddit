package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private Privilege privilege;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User() {
        this.privilege = Privilege.USER;
        this.posts = new ArrayList<>();
    }

    public User(String name, String email, String password) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
