package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;

    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;

    public Vote() {}

    public Vote(int value, User user, Post post) {
        this.value = value;
        this.user = user;
        this.post = post;
    }
}
