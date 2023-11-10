package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import com.greenfoxacademy.reddit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {
    private PostRepository posts;
    private PostService postService;
    private UserRepository users;
    private UserService userService;

    @Autowired
    public DbService(PostRepository posts, UserRepository users,
                     PostService postService, UserService userService) {
        this.posts = posts;
        this.users = users;
        this.postService = postService;
        this.userService = userService;
    }


    public void addPost(String postTitle, String postDescription) {
        Post newPost = new Post(postTitle, postDescription);
        newPost.setUser(userService.getLoggedInUser());
        postService.save(newPost);
        userService.getLoggedInUser().addPost(newPost);
        userService.save(userService.getLoggedInUser());
    }
}
