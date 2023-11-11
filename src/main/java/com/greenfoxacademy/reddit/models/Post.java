package com.greenfoxacademy.reddit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;


@Setter
@Getter
@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Timestamp createDate;

    @OneToMany(mappedBy = "post")
    private List<Vote> votes;

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

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void deleteVote(Vote vote) {
        this.votes.remove(vote);
    }

    @Override
    public String toString() {
        return this.title;
    }

    public int postScore() {
        return votes.stream()
                .mapToInt(Vote::getValue)
                .sum();
    }

}
