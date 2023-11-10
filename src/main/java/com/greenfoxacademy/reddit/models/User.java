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
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Privilege privilege;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @ManyToMany(mappedBy = "upvoters")
    private Set<Post> upvotedPosts;

    @ManyToMany(mappedBy = "downvoters")
    private Set<Post> downvotedPosts;


    public User() {
        this.privilege = Privilege.USER;
        this.posts = new ArrayList<>();
        this.upvotedPosts = new HashSet<>();
        this.downvotedPosts = new HashSet<>();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }
}
