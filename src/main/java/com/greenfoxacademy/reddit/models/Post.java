package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="up_votes",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<User> upvoters;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="down_votes",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> downvoters;


    public Post() {
        this.upvoters = new HashSet<>();
        this.downvoters = new HashSet<>();
    }

    public Post(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
