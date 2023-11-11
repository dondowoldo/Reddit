package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository posts;

    @Autowired
    public PostService(PostRepository posts) {
        this.posts = posts;
    }

    public Post save(Post post) {
        return posts.save(post);
    }

    public Optional<Post> findById(Long postId) {
        return posts.findById(postId);
    }

    public List<Post> findAll() {
        return posts.findAll();
    }

    public List<Post> findAllDescOrder(String search) {
        return posts.findAll()
                .stream()
                .filter(p -> search == null ? true : (
                        p.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                        p.getDescription().toLowerCase().contains(search.toLowerCase()))
                )
                .sorted(Comparator.comparing(Post :: getCreateDate)
                .reversed())
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Long id) {
        return posts.findById(id);
    }

}
