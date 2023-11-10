package com.greenfoxacademy.reddit.controllers;

import com.greenfoxacademy.reddit.models.Post;
import com.greenfoxacademy.reddit.models.User;
import com.greenfoxacademy.reddit.services.DbService;
import com.greenfoxacademy.reddit.services.PostService;
import com.greenfoxacademy.reddit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {
    private UserService userService;
    private PostService postService;
    private DbService dbService;

    @Autowired
    public WebController(UserService userService, PostService postService, DbService dbService) {
        this.userService = userService;
        this.postService = postService;
        this.dbService = dbService;
    }

    @GetMapping({"/", "/posts"})
    public String index(Model model, String search) {

        model.addAttribute("isLoggedIn", userService.loggedIn());
        model.addAttribute("posts", postService.findAllDescOrder(search));
        model.addAttribute("searched", search);
        return "index";
    }

    @GetMapping("/posts/add")
    public String addPostPage() {
        return userService.loggedIn() ? "add-post" : "redirect:/login";
    }

    @PostMapping ("/posts/add")
    public String addPostSubmit(String title, String description) {
        if (!userService.loggedIn()) {
            return "redirect:/";
        }
        dbService.addPost(title, description);
        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {
        if (userService.loggedIn()) {
            return "index";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(String email, String password) {
        if (userService.loginUser(email, password)) {
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logOut();
        return "redirect:/";
    }

}
