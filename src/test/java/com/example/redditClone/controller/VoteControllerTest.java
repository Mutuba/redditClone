package com.example.redditClone.controller;


import com.example.redditClone.dto.CommentRequest;
import com.example.redditClone.dto.VoteDTO;
import com.example.redditClone.models.*;
import com.example.redditClone.repository.CommentRepository;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.repository.VoteRepository;
import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.CustomUserDetailsService;
import com.example.redditClone.service.MailService;
import com.example.redditClone.service.UserPrincipal;
import groovy.transform.AutoClone;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static groovy.json.JsonOutput.toJson;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PostRepository postRepository;


    @MockBean
    VoteRepository voteRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    CustomUserDetailsService customUserDetailsService;



    @Test
    public void shouldReturnOkForUpVote() throws Exception {
        User user = createUser();

        Post actualPost = Post.builder()
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

        Vote actualVote = new Vote(123L, VoteType.UPVOTE, actualPost, user);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(actualPost));


        when(voteRepository.save(any(Vote.class))).thenReturn(actualVote);
        when(postRepository.save(any(Post.class))).thenReturn(actualPost);

        // Act & Assert
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVoteType(VoteType.UPVOTE);
        voteDTO.setPostId(actualPost.getPostId());

        String token = authSetUp();
        String uri = "/api/votes/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(voteDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    public void shouldReturnOkForDownVote() throws Exception {
        User user = createUser();

        Post actualPost = Post.builder()
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

        Vote actualVote = new Vote(123L, VoteType.DOWNVOTE, actualPost, user);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(actualPost));


        when(voteRepository.save(any(Vote.class))).thenReturn(actualVote);
        when(postRepository.save(any(Post.class))).thenReturn(actualPost);

        // Act & Assert
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVoteType(VoteType.DOWNVOTE);
        voteDTO.setPostId(actualPost.getPostId());

        String token = authSetUp();
        String uri = "/api/votes/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(voteDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    public void shouldReturnOkForDownVoteWhenUserHadUpVoted() throws Exception {
        User user = createUser();

        Post actualPost = Post.builder()
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

        Vote actualVote = new Vote(123L, VoteType.UPVOTE, actualPost, user);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(actualPost));

        when(voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                any(Post.class), any(User.class))).thenReturn(Optional.of(actualVote));

        when(voteRepository.save(any(Vote.class))).thenReturn(actualVote);
        when(postRepository.save(any(Post.class))).thenReturn(actualPost);

        // Act & Assert
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setVoteType(VoteType.DOWNVOTE);
        voteDTO.setPostId(actualPost.getPostId());

        String token = authSetUp();
        String uri = "/api/votes/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(voteDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
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
