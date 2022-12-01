package com.codeup.springblog.controllers;

import com.codeup.springblog.models.User;
import com.codeup.springblog.services.EmailService;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import com.codeup.springblog.models.Post;
import com.codeup.springblog.services.FileUploadService;
import com.codeup.springblog.utils.Utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    // Repositories and Services
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final EmailService emailService;
    private final FileUploadService fileUploadService;

    // Constructor
    public PostController(
            PostRepository postDao,
            UserRepository userDao,
            EmailService emailService,
            FileUploadService fileUploadService)
    {
        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
        this.fileUploadService = fileUploadService;
    }

    // Displays full list of all posts
    @GetMapping("/posts")
    public String postsIndex(Model model) {
        // Sets a post list and passes it to the template
        List<Post> posts = postDao.findAll();
        model.addAttribute("posts", posts);
        return "posts/index";
    }

    // Displays individual post
    @GetMapping("/posts/{id}")
    public String individualPost(@PathVariable long id, Model model) {
        // Checks if post id exists, if not redirects to post index
        if(postDao.findById(id) == null) {
            return "redirect:/posts";
        }
        // Sets individual post by id parameter, passes it to template
        Post post = postDao.findById(id);
        model.addAttribute("post", post);
        return "posts/show";
    }

    // Displays form to create a post
    @GetMapping("/posts/create")
    public String showCreatePostForm(Model model) {
        // Passing empty post to form
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    // Creates new post, uploads file, and adds row to posts table
    @PostMapping("/posts/create")
    public String createPost(
            @ModelAttribute @Valid Post post,
            @RequestParam(name = "file") MultipartFile uploadedFile,
            Errors validation,
            Model model)
    {
        // Enforces post parameters of size and not being empty
        if(validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("post", post);
            return "posts/create";
        }

        // Sets the user of the post to be the logged in user, uploads the file in the form, then saves post into table
        post.setUser(Utils.currentUser());
        postDao.save(post);
        fileUploadService.uploadFile(uploadedFile, post, model);
        postDao.save(post);

        // Using emailService to send an email out of created post
        emailService.prepareAndSend(post, "Post Created", "" +
                "A new post has been successfully created" +
                "New Post:" +
                "\n\n" +
                post.getTitle() +
                "\n" +
                post.getBody());
        return "redirect:/posts";
    }

    // Shows form to edit a post
    @GetMapping("/posts/{id}/edit")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Post post = postDao.findById(id).get();
        // check if user is owner of post
        if(!post.getUser().equals(Utils.currentUser())) {
            return "redirect:/posts";
        }
        // Setting post by id in URL path and passes it to template
        model.addAttribute("post", post);
        return "posts/edit";
    }

    // Takes changes to post and alters posts table
    @PostMapping("/posts/{id}/edit")
    public String editPost(@ModelAttribute @Valid Post post, Errors validation, Model model) {
        // Enforces post parameters of size and not being empty
        if(validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("post", post);
            return "/posts/edit";
        }

        // Sets post user by who is logged in at the time
        post.setUser(Utils.currentUser());
        postDao.save(post);

        // Using emailService to send email of edited post
        emailService.prepareAndSend(post, "Post Edited", "" +
                "Your post has been successfully edited." +
                "\n" +
                "New Post:" +
                "\n\n" +
                post.getTitle() +
                "\n" +
                post.getBody());
        return ("redirect:/posts/" + post.getId());
    }

    // Shows form to delete a post
    @GetMapping("/posts/{id}/delete")
    public String showDeletePost(@PathVariable Long id, Model model) {
        // Sets post by id in URL path, sends post to template
        Post post = postDao.findById(id).get();
        model.addAttribute("post", post);
        return"/posts/delete";
    }

    // Removes post
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@ModelAttribute Post post) {
        postDao.delete(post);
        return("redirect:/profile/");
    }

    // Views all posts in JSON format
    @GetMapping("/posts.json")
    public @ResponseBody List<Post> viewAllPostsInJSONFormat() {
        // Creating posts list and returning
        List<Post> posts = postDao.findAll();
        return posts;
    }

    // Views all posts with Ajax
    @GetMapping("/posts/ajax")
    public String viewAllPostsWithAjax() {
        return "posts/ajax";
    }
}
