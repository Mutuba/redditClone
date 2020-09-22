package com.example.redditClone.controller;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.SubredditRepository;
import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.AuthService;
import com.example.redditClone.service.CustomUserDetailsService;
import com.example.redditClone.service.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
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
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class SubredditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    SubredditRepository subredditRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    AuthService authService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void addSubreddit_ShouldReturn_Created_Subreddit() throws Exception {
        UserPrincipal userPrincipal = createPrincipal();
        String token = authToken();
        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userPrincipal.getId());

        Mockito.when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);

        Subreddit actualSubreddit = Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("I love you")
                .creationDate(Instant.now())
                .build();
        Mockito.when(subredditRepository.save(Mockito.any(Subreddit.class))).thenReturn(actualSubreddit);
        Mockito.when(authService.getCurrentUser()).thenReturn(new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        ));


        // Act & Assert

        SubredditDTO subredditDTO = SubredditDTO.builder().name("Love").description(
                "I love you"
        ).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddit")
                .header("Authorization", "Bearer " + token)
                .content(toJson(subredditDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Love"))
                .andExpect(jsonPath("$.description").value("I love you"));
    }


    @Test
    public void getAllSubreddits_ShouldReturn_List_of_Subreddits() throws Exception {
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

        List<Subreddit> actualSubredditList = Arrays.asList(Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("What the fuck")
                .creationDate(Instant.now())
                .posts(Arrays.asList(
                        new Post(
                                "Love",
                                "http://127.0.0.1:8000/api/wallet/create",
                                "What a thing",
                                23,
                                Instant.now())))
                .build());

        Mockito.when(subredditRepository.findAll()).thenReturn(actualSubredditList);
        String uri = "/api/subreddit";
        mockMvc.perform(MockMvcRequestBuilders
                .get(uri).header("Authorization", "Bearer " + token))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk());
    }


    @Test
    public void getSubreddit_ShouldReturn_Found_Subreddit() throws Exception {
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

        Subreddit actualSubreddit = Subreddit.builder()
                .id(123L)
                .name("iPhone12")
                .description("The best smartphone ever")
                .creationDate(Instant.now())
                .posts(Arrays.asList(
                        new Post(
                                "Love",
                                "http://127.0.0.1:8000/api/wallet/create",
                                "What a thing",
                                23,
                                Instant.now())))
                .build();

        Mockito.when(subredditRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(actualSubreddit));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit/123").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("iPhone12"))
                .andExpect(jsonPath("$.description").value("The best smartphone ever"));
    }


    @Test
    public void shouldRaiseUnAuthorisedIfTokenIsNotInHeaders() throws Exception {
        Subreddit actualSubreddit = Subreddit.builder()
                .id(123L)
                .name("iPhone12")
                .description("The best smartphone ever")
                .creationDate(Instant.now())
                .posts(Arrays.asList(
                        new Post(
                                "Love",
                                "http://127.0.0.1:8000/api/wallet/create",
                                "What a thing",
                                23,
                                Instant.now())))
                .build();

        Mockito.when(subredditRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(actualSubreddit));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit/123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getSubreddit_ShouldReturn_404_Not_Found_For_Non_Existent_Subreddit() throws Exception {
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


        Mockito.when(subredditRepository.findById(Mockito.anyLong()))
                .thenThrow(new SubredditNotFoundException("Subreddit not found with id -0"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit/123").header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error").value("Subreddit not found with id -0"))
                .andExpect(status().isNotFound());
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
