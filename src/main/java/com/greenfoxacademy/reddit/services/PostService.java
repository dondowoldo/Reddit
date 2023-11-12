package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Predicate<Post> searchFilter = p -> search == null ? true : (
                p.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                p.getDescription().toLowerCase().contains(search.toLowerCase()) ||
                p.getUser().getName().toLowerCase().contains(search.toLowerCase())
        );

        if (posts.findAll().size() < 10) {
            return posts.findAll().stream()
                    .filter(searchFilter)
                    .sorted(Comparator.comparing(Post :: postScore)
                            .thenComparing(Post :: getCreateDate)
                            .reversed())
                    .collect(Collectors.toList());
        }

        Stream<Post> bestRated = posts.findAll()
                .stream()
                .filter(searchFilter)
                .sorted(Comparator.comparing(Post :: postScore)
                        .reversed()
                        .thenComparing(Post :: getCreateDate).reversed())
                .limit(10);

        Stream<Post> restByCreation = posts.findAll()
                .stream()
                .filter(searchFilter)
                .sorted(Comparator.comparing(Post :: getCreateDate)
                        .reversed());

        return Stream.concat(bestRated, restByCreation)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Long id) {
        return posts.findById(id);
    }

}
