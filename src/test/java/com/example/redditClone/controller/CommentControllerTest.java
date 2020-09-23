package com.example.redditClone.controller;

import com.example.redditClone.dto.CommentRequest;
import com.example.redditClone.models.*;
import com.example.redditClone.repository.CommentRepository;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.CustomUserDetailsService;
import com.example.redditClone.service.MailService;
import com.example.redditClone.service.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.*;

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    MailService mailService;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void shouldReturnCreatedSuccessWhenAddPostMethodIsCalled() throws Exception {
        User user = createUser();

        Post post = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("I love you")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(user)
                .subreddit(Subreddit
                        .builder()
                        .id(123L)
                        .name("Love")
                        .description("I love you")
                        .creationDate(Instant.now())
                        .build())
                .build();

        Comment comment = Comment.builder().text("I really love this post").post(post).user(user).build();

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        doNothing().when(mailService).sendEmail(any(NotificationEmail.class));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        // Act & Assert
        CommentRequest commentRequest = CommentRequest.builder()
                .text("Your post is amazing")
                .postId(123l)
                .build();


        String token = authSetUp();
        String uri = "/api/comments/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(commentRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    public void shouldReturnAlistOfCommentsBasedOnPostId() throws Exception {
        User user = createUser();

        Post post = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("I love you")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(user)
                .subreddit(Subreddit
                        .builder()
                        .id(123L)
                        .name("Love")
                        .description("I love you")
                        .creationDate(Instant.now())
                        .build())
                .build();

        List<Comment> comment = new ArrayList<>(
                Arrays.asList(
                Comment.builder()
                        .id(123l)
                        .text("I really love this post")
                        .post(post)
                        .user(user)
                        .creationDate(Instant.now())
                        .build()
                )
        );

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        when(commentRepository.findByPost(any(Post.class))).thenReturn(comment);
        // Act & Assert

        String token = authSetUp();
        String uri = "/api/comments/by-post/123";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldReturnAlistOfCommentsBasedOnUsername() throws Exception {
        User user = createUser();
        Post post = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("I love you")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(user)
                .subreddit(Subreddit
                        .builder()
                        .id(123L)
                        .name("Love")
                        .description("I love you")
                        .creationDate(Instant.now())
                        .build())
                .build();

        List<Comment> comment = new ArrayList<>(
                Arrays.asList(
                        Comment.builder()
                                .id(123l)
                                .text("I really love this post")
                                .post(post)
                                .user(user)
                                .creationDate(Instant.now())
                                .build()
                )
        );

        when(commentRepository.findAllByUser(any(User.class))).thenReturn(comment);

        // Act & Assert
        String token = authSetUp();
        String uri = "/api/comments/by-user/Mutush";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Helper function

    public String authToken() {
        return UUID.fromString("00000000-000-0000-0000-000000000001").toString();
    }

    public UserPrincipal createPrincipal() {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        return new UserPrincipal(123L,
                "Mutush", "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);

    }


    public User createUser(){
        return new User("Mutush", "daniel@gmail.com", "Baraka1234");
    }


    public String authSetUp() {
        // Arrange
        User user = createUser();

        UserPrincipal userPrincipal = createPrincipal();

        when(customUserDetailsService.loadUserById(anyLong())).thenReturn(userPrincipal);
        // safe to control an external dependency in tests
        // the user becomes the current user of the app
        when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(Boolean.TRUE);

        String token = authToken();
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userPrincipal.getId());

        return token;
    }

}
