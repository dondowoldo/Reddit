package com.greenfoxacademy.reddit.services;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {
    private DbService dbService;

    @Autowired
    public PostService(DbService dbService) {
        this.dbService = dbService;
    }

    public Optional<Post> getPostById(Long id) {
        return dbService.getPostById(id);
    }

    public List<Post> getAllPosts() {
        return dbService.getPosts().findAll();
    }

    public void addPost(String postTitle, String postDescription, URL url) {
        dbService.addPost(postTitle, postDescription, url);
    }
    public void deletePostById(Long postId) {
        dbService.deletePostById(postId);
    }

    public List<Post> findAllDescOrder(String search) {
        final int TOP_RATED = 10;
        Predicate<Post> searchFilter = p -> search == null ? true : (
                p.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                p.getDescription().toLowerCase().contains(search.toLowerCase()) ||
                p.getUser().getName().toLowerCase().contains(search.toLowerCase())
        );
        List<Post> allPosts = getAllPosts();

        if (getAllPosts().size() < TOP_RATED) {
            return allPosts.stream()
                    .filter(searchFilter)
                    .sorted(Comparator.comparing(Post :: postScore)
                            .thenComparing(Post :: getCreateDate)
                            .reversed())
                    .collect(Collectors.toList());
        }

        Stream<Post> bestRated = allPosts
                .stream()
                .filter(searchFilter)
                .sorted(Comparator.comparing(Post :: postScore)
                        .thenComparing(Post :: getCreateDate)
                        .reversed())
                .limit(TOP_RATED);

        Stream<Post> restByCreation = allPosts
                .stream()
                .filter(searchFilter)
                .sorted(Comparator.comparing(Post :: getCreateDate)
                        .reversed());

        return Stream.concat(bestRated, restByCreation)
                .distinct()
                .collect(Collectors.toList());
    }


}
