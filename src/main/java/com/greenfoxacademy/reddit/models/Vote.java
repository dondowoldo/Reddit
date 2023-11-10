package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;

@Entity
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;

    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
}
