package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.models.Vote;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import com.greenfoxacademy.reddit.repositories.VoteRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

@Getter
@Service
public class DbService {
    private PostRepository posts;
    private UserRepository users;
    private VoteRepository votes;

    @Autowired
    public DbService(PostRepository posts,
                     UserRepository users,
                     VoteRepository votes) {
        this.posts = posts;
        this.users = users;
        this.votes = votes;
    }


    public void addPost(String postTitle, String postDescription, URL url) {
        User user = UserService.getCurrentUser();
        Post newPost = new Post(postTitle, postDescription, url);

        newPost.setUser(user);
        posts.save(newPost);
        user.addPost(newPost);
        users.save(user);
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
                postToDelete.get().getUser().getId() == UserService.getCurrentUser().getId()) {
            votes.deleteAll(postToDelete.get().getVotes());
            posts.deleteById(postId);
        }
    }
    public Optional<Post> getPostById(Long id) {
        return posts.findById(id);
    }
}
