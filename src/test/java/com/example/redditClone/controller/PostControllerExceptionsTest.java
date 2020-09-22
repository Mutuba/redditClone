package com.example.redditClone.controller;

import com.example.redditClone.dto.PostRequest;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.models.User;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebAppConfiguration
@AutoConfigureMockMvc
public class PostControllerExceptionsTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    AuthService authService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test()
    public void shouldRaiseUserNotFoundWhenGetPostByUsernameIsCalledWithNonExistingUsername()
            throws Exception {

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


        // Act & Assert
        String uri = "/api/posts/user/mutuba/";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error")
                        .value("User not found with username: mutuba"));

    }


    @Test
    public void shouldRaiseSubredditNotFoundWhenGetPostByIDIsCalledWithNonExistingSubredditID()
            throws Exception {

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

        // Act & Assert

        String uri = "/api/posts/subreddit/123/";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("Subreddit not found with id: 123"));

    }



    @Test
    public void shouldRaisePostNotFoundWhenGetPostByIDIsCalledWithNonExistingID()
            throws Exception {

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

        // Act & Assert
        String uri = "/api/posts/123/";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("Post not found with id: 123"));

    }


    @Test
    public void shouldRaiseSubredditNotFoundWhenGetPostByIDIsCalledWithNonExistingID()
            throws Exception {

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



        // Act & Assert
        PostRequest postRequest = PostRequest.builder()
                .postTitle("Love")
                .description("What the fuck")
                .url("http://localhost:5000/api/auth//user/me")
                .subredditName("Love")
                .build();

        String uri = "/api/posts";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .content(toJson(postRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("Love"));

    }


    // Helper functions

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
