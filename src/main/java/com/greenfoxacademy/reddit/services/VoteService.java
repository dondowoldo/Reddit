package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Vote;
import com.greenfoxacademy.reddit.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private VoteRepository votes;

    @Autowired
    public VoteService(VoteRepository votes) {
        this.votes = votes;
    }

    public Vote save(Vote vote) {
        return votes.save(vote);
    }

    public void delete(Vote vote) {
        votes.delete(vote);
    }

}
