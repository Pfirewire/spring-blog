package com.codeup.springblog;

import com.codeup.springblog.models.Post;
import com.codeup.springblog.models.User;
import com.codeup.springblog.repositories.PostRepository;
import com.codeup.springblog.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBlogApplication.class)
@AutoConfigureMockMvc
public class PostsIntegrationTests {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository userDao;
    @Autowired
    PostRepository postDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testExerciseUser");

        if(testUser == null) {
            User newUser = new User();
            newUser.setUsername("testExerciseUser");
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setEmail("testExerciseUser@email.com");
            testUser = userDao.save(newUser);
        }

        httpSession = this.mvc.perform
                        (post("/login").with(csrf())
                                .param("username", "testExerciseUser")
                                .param("password", "password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/posts"))
                .andReturn()
                .getRequest()
                .getSession();

    }

    @Test
    public void contextLoads() {
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        assertNotNull(httpSession);
    }

    @Test
    public void testCreatePost() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/pfirewire/Codeup/Projects/spring-blog/src/main/webapp/28megamanicon.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileInputStream);

        this.mvc.perform(
                multipart("/posts/create")
                        .file(multipartFile)
                        .with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "test post title")
                        .param("body", "test post body"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testShowPost() throws Exception {
        Post existingPost = postDao.findAll().get(0);

        this.mvc.perform(
                get("/posts/" + existingPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(existingPost.getBody())));
    }

    @Test
    public void testShowAllPosts() throws Exception {
        Post existingPost = postDao.findAll().get(0);

        this.mvc.perform(
                get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("All Posts")))
                .andExpect(content().string(containsString(existingPost.getTitle())));
    }

    @Test
    public void testEditPost() throws Exception {
        Post existingPost = postDao.findAll().get(0);

        this.mvc.perform(
                post("/posts/" + existingPost.getId() + "/edit").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "edited title exercise")
                        .param("body", "edited body"))
                .andExpect(status().is3xxRedirection());

        this.mvc.perform(
                get("/posts/" + existingPost.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("edited title exercise")))
                .andExpect(content().string(containsString("edited body")));
    }

    @Test
    public void testDeletePost() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/pfirewire/Codeup/Projects/spring-blog/src/main/webapp/28megamanicon.png");
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileInputStream);

        this.mvc.perform(
                        multipart("/posts/create")
                                .file(multipartFile).with(csrf())
                                .session((MockHttpSession) httpSession)
                                .param("title", "test post to be deleted")
                                .param("body", "test post body"))
                .andExpect(status().is3xxRedirection());

        Post existingPost = postDao.findByTitle("test post to be deleted");

        this.mvc.perform(
                post("/posts/" + existingPost.getId() + "/delete").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("id", String.valueOf(existingPost.getId())))
                .andExpect(status().is3xxRedirection());
    }

}
