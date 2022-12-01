package com.codeup.springblog.controllers;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    // Repositories and Services
    private UserRepository userDao;
    private PostRepository postDao;
    private PasswordEncoder passwordEncoder;

    // Constructor
    public UserController(UserRepository userDao, PostRepository postDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.passwordEncoder = passwordEncoder;
    }

    // Shows form to sign up as a user
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        // Sending empty user to template
        model.addAttribute("user", new User());
        return "users/signup";
    }

    // Saves user to users table
    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user, @RequestParam(value = "confirmPassword") String confirmPassword) {
        // Hashing password
        String hash = passwordEncoder.encode(user.getPassword());
        // Checking if password and confirm password are the same
        if(passwordEncoder.matches(confirmPassword, hash)) {
            // Setting user password to the hash and saving user to table
            user.setPassword(hash);
            userDao.save(user);
            return "redirect:/login";
        } else {
            // Redirecting user back to signup form
            return "users/signup";
        }
    }

    // Shows user profile
    // At the moment this just shows the list of all posts user has created
    @GetMapping("/profile")
    public String showUserPosts(Model model) {
        // Creating a list of all posts and empty post to populate
        List<Post> allPosts = postDao.findAll();
        List<Post> userPosts = new ArrayList<>();

        // Setting user to currently logged in user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Iterates through all posts and adds to new list if created by logged in user
        for(Post post : allPosts) {
            if(Objects.equals(post.getUserId(), user.getId())) {
                userPosts.add(post);
            }
        }

        // Sending user created post list to template
        model.addAttribute("posts", userPosts);
        return "/users/profile";
    }

    @GetMapping("/users/{id}")
    public String showSingleUserPosts(@PathVariable Long id, Model model) {
        // Creating a list of all posts and empty post to populate
        List<Post> allPosts = postDao.findAll();
        List<Post> userPosts = new ArrayList<>();

        // Setting user to currently logged in user
        User user = userDao.findById(id).get();

        // Iterates through all posts and adds to new list if created by logged in user
        for (Post post : allPosts) {
            if (Objects.equals(post.getUserId(), user.getId())) {
                userPosts.add(post);
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        return "users/profile";
    }

}
