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
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Timestamp createDate;

    @ManyToOne
    private User user;


    public Post() {
        this.createDate = new Timestamp(System.currentTimeMillis());
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
