package com.example.redditClone.controller;


import com.example.redditClone.dto.PostRequest;
import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.SubredditRepository;
import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.AuthService;
import com.example.redditClone.service.CustomUserDetailsService;
import com.example.redditClone.service.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebAppConfiguration
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    SubredditRepository subredditRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    AuthService authService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void add_Post_ShouldReturn_Created_Subreddit() throws Exception {
        UserPrincipal userPrincipal = createPrincipal();
        String token = authToken();
        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userPrincipal.getId());

        Mockito.when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);

        Mockito.when(authService.getCurrentUser()).thenReturn(new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        ));


        List<Post> postList = new ArrayList<>(Arrays.asList());

        Subreddit actualSubreddit = Subreddit
                .builder()
                .id(123L)
                .name("Love")
                .description("I love you")
                .creationDate(Instant.now())
                .posts(postList)
                .build();

        Mockito.when(
                subredditRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(actualSubreddit)
                );

        Post actualPost =Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("I love you")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(
                        new User("Mutush",
                        "daniel@gmail.com",
                        passwordEncoder.encode("Baraka1234")
                        )
                )
                .subreddit(actualSubreddit)
                .build();


        // Act & Assert
        PostRequest postRequest = PostRequest.builder()
                .postTitle("Love")
                .description("What the fuck")
                .url("http://localhost:5000/api/auth//user/me")
                .subredditName("Love")
                .build();

        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(actualPost);
        String uri = "/api/posts";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(postRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }




    /**
     * Return an Auth Token.
     *
     * @return The result as a string.
     * @throws Exception if you got any of the above wrong.
     */


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

}
