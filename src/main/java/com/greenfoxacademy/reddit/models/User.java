package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private Role role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts;

    public User() {
        this.role = Role.USER;
        this.posts = new ArrayList<>();
        this.votes = new ArrayList<>();
    }

    public User(String name, String email, String password) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void deleteVote(Vote vote) {
        this.votes.remove(vote);
    }

    public Integer getPostVoteValue(Post post) {
        return post.getVotes().stream()
                .filter(v -> Objects.equals(v.getUser().getId(), this.id))
                .map(Vote::getValue)
                .findFirst().orElse(0);
    }
}
