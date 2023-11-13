package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private DbService dbService;

    @Autowired
    public VoteService(DbService dbService) {
        this.dbService = dbService;
    }

    public void addVote(Integer incomingVoteValue, Long postId) {
        dbService.addVote(incomingVoteValue, postId, UserService.getCurrentUser());
    }
}
