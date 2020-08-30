package com.example.redditClone;

import com.example.redditClone.dto.*;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.AuthService;
import com.example.redditClone.service.CustomUserDetailsService;
import com.example.redditClone.service.SubredditService;
import com.example.redditClone.service.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static groovy.json.JsonOutput.toJson;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthService authService;

    @MockBean
    private JavaMailSender sender;

    public AuthControllerTest() {
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void userSigningUpReturnsCreated() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void userSignUpFailsIfUsernameIsTaken() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

        when(userRepository.existsByUsername(registrationRequest.getUsername()))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void userSignUpFailsIfEmailIsTaken() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = registrationRequest();

        Mockito.when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(true);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void userLoginSuccessfulWhenAccountExists() throws Exception {
        LoginRequest loginRequest = loginRequest();
        Mockito.when(authService.login(loginRequest)).thenReturn(new AuthenticationResponse(
                "srdtfyuigcvjrvuevqyr", "Mutuba"));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("Mutuba"));
    }



    @Test
    public void userLoginUnsuccessfulWhenWrongAccountDetails() throws Exception {
        LoginRequest loginRequest = loginRequest();
        Mockito.when(authService.login(loginRequest)).thenThrow(new BadCredentialsException("Bad credentials"));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }



    // Utility functions used in the test class

    /**
     * Return registrationRequest .
     *
     * @return The registrationRequest object.
     */

    public RegistrationRequest registrationRequest() {
        return new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

    }


    /**
     * Return LoginRequest.
     *
     * @return The loginRequest object.
     */

    public LoginRequest loginRequest() {
        return new LoginRequest("Mutush", "Baraka1234");

    }

    /**
     * Return an Auth Token.
     *
     * @return The result as a string.
     * @throws Exception if you got any of the above wrong.
     */


    public String authToken() {

        String token = "wqerwtytyjukilroli7ruktyrtrbrntj";
        return token;
    }

}
