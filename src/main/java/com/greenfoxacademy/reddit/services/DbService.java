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

import java.net.URL;
import java.util.Optional;

@Service
public class DbService {
    private PostRepository posts;
    private UserRepository users;
    private UserService userService;
    private VoteRepository votes;

    @Autowired
    public DbService(PostRepository posts, UserRepository users,
                     UserService userService,
                     VoteRepository votes) {
        this.posts = posts;
        this.users = users;
        this.votes = votes;
        this.userService = userService;
    }


    public void addPost(String postTitle, String postDescription, URL url) {
        Post newPost = new Post(postTitle, postDescription, url);
        newPost.setUser(userService.getLoggedInUser());
        posts.save(newPost);
        userService.getLoggedInUser().addPost(newPost);
        userService.save(userService.getLoggedInUser());
    }

    public void addVote(Integer incomingVoteValue, Long postId, User loggedUser) {
        Optional<Post> post = posts.findById(postId);
        if (post.isEmpty() || loggedUser == null || incomingVoteValue == null) {
            return;
        }

        Optional<Vote> oldVote = voteMade(post.get(), loggedUser);
        if (oldVote.isEmpty()) {
            incomingVoteValue = incomingVoteValue > 1 ? 1 : incomingVoteValue;
            incomingVoteValue = incomingVoteValue < -1 ? -1 : incomingVoteValue;
            Vote vote = new Vote(incomingVoteValue, loggedUser, post.get());
            votes.save(vote);
            post.get().addVote(vote);
            loggedUser.addVote(vote);
        } else if (oldVote.get().getValue() == incomingVoteValue) {
            post.get().deleteVote(oldVote.get());
            loggedUser.deleteVote(oldVote.get());
            votes.delete(oldVote.get());
        }
    }
    public Optional<Vote> voteMade(Post post, User loggedUser){
        return post.getVotes().stream()
                .filter(v -> v.getUser().getId().equals(loggedUser.getId()))
                .findFirst();
    }

    public void deletePostById(Long postId) {
        Optional<Post> postToDelete = posts.findById(postId);
        if (postToDelete.isPresent() &&
                postToDelete.get().getUser().getId() == userService.getLoggedInUser().getId()) {
            postToDelete.get().getVotes()
                    .forEach(v -> votes.deleteById(v.getId()));
            posts.deleteById(postId);
        }
    }
}
