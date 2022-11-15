package com.codeup.springblog.models;

import com.codeup.springbootexercises.models.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="posts")
public class Post {


    // All private variables, attached to posts table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Post must have a title")
    @Size(min = 3, message = "Title must be at least 3 characters")
    @Column(nullable = false)
    private String title;
    @NotBlank(message = "Post must have something in the body")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String filename;


    // Constructor functions
    public Post() {}
    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }


    // Getters and Setters
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return this.title; }
    public void setTitle(String title){ this.title = title; }

    public String getBody() { return this.body; }
    public void setBody(String body) { this.body = body; }

    public User getUser() { return this.user; }
    public void setUser(User user) { this.user = user; }

    public Long getUserId() { return this.user.getId(); }

    public String getFilename() { return this.filename; }
    public void setFilename(String filename) { this.filename = filename; }

}
