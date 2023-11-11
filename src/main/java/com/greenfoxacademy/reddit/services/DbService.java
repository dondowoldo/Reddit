package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.models.Vote;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import com.greenfoxacademy.reddit.repositories.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DbService {
    private PostRepository posts;
    private PostService postService;
    private UserRepository users;
    private UserService userService;
    private VoteService voteService;
    private VoteRepository votes;

    @Autowired
    public DbService(PostRepository posts, UserRepository users,
                     PostService postService, UserService userService,
                     VoteService voteService, VoteRepository votes) {
        this.posts = posts;
        this.users = users;
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
        this.votes = votes;
    }


    public void addPost(String postTitle, String postDescription) {
        Post newPost = new Post(postTitle, postDescription);
        newPost.setUser(userService.getLoggedInUser());
        postService.save(newPost);
        userService.getLoggedInUser().addPost(newPost);
        userService.save(userService.getLoggedInUser());
    }

    public void addVote(Integer incomingVoteValue, Long postId, User loggedUser) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty() || loggedUser == null || incomingVoteValue == null) {
            return;
        }

        Optional<Vote> oldVote = voteMade(post.get(), loggedUser);
        if (oldVote.isEmpty()) {
            Vote vote = new Vote(incomingVoteValue, loggedUser, post.get());
            voteService.save(vote);
            post.get().addVote(vote);
            loggedUser.addVote(vote);
        } else if (oldVote.get().getValue() == incomingVoteValue) {
            post.get().deleteVote(oldVote.get());
            loggedUser.deleteVote(oldVote.get());
            voteService.delete(oldVote.get());
        }
    }
    public Optional<Vote> voteMade(Post post, User loggedUser){
        return post.getVotes().stream()
                .filter(v -> v.getUser().getId().equals(loggedUser.getId()))
                .findFirst();
    }
}
