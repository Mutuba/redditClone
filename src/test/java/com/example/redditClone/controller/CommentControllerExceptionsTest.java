package com.example.redditClone.controller;

import com.example.redditClone.dto.CommentRequest;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.security.JwtTokenProvider;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static groovy.json.JsonOutput.toJson;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerExceptionsTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;


    @MockBean
    UserRepository userRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void shouldThrowPostNotFoundExceptionWhenPostIdDoesNotExist() throws Exception {
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error")
                        .value("Post not found with id: " + 123));
        ;
    }


    @Test
    public void shouldThrowPostNotFoundWhenGettingListOfCommentsBasedOnPostIdWhenPostIDDoesNotExist()
            throws Exception {
        // Act & Assert
        String token = authSetUp();
        String uri = "/api/comments/by-post/123";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error")
                        .value("Post not found with id: " + 123));
    }


    @Test
    public void shouldThrowUserNotFoundExceptionWhenGettingListOfCommentsBasedOnUsernameIfUsernameDoesNotExist()
            throws Exception {
        // Act & Assert
        String token = authSetUp();
        String uri = "/api/comments/by-user/Mutuba";
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error")
                        .value("User not found with: Mutuba"));
    }


    // Helper functions
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


    public String authSetUp() {
        // Arrange
        User user = new User("Mutush", "daniel@gmail.com", "Baraka1234");

        UserPrincipal userPrincipal = createPrincipal();

        when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);
        // safe to control an external dependency in tests
        when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);

        String token = authToken();
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userPrincipal.getId());

        return token;
    }
}
