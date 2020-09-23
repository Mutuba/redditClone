package com.example.redditClone.controller;

import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.exception.UsernameNotFoundException;
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
import org.springframework.security.authentication.BadCredentialsException;
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

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class AuthControllerLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void userShouldLoginSuccessfullyWhenAccountExists() throws Exception {
        UserPrincipal userPrincipal = createPrincipal();
        Mockito.when(customUserDetailsService.loadUserByUsername(
                Mockito.anyString())).thenReturn(userPrincipal);
        LoginRequest loginRequest = new LoginRequest("Mutush", "Baraka1234");

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("Mutush"));
    }


    @Test
    public void userLoginShouldBeUnsuccessfulWhenWrongPasswordIsUsed() throws Exception {

        UserPrincipal userPrincipal = createPrincipal();
        Mockito.when(customUserDetailsService.loadUserByUsername(
                Mockito.anyString())).thenReturn(userPrincipal);

        LoginRequest loginRequest = new LoginRequest("Mutush", "Baraka12345");

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }


    @Test
    public void userLoginShouldBeUnsuccessfulWhenWrongAccountDetailsAreUsed() throws Exception {
        Mockito.when(customUserDetailsService.loadUserByUsername(
                Mockito.anyString())).thenThrow(new BadCredentialsException("Bad credentials"));
        LoginRequest loginRequest = new LoginRequest("Mutuba", "Baraka1234");
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }


    // Utility functions used in the test class

    /**
     * Return LoginRequest.
     *
     * @return The loginRequest object.
     */

    public UserPrincipal createPrincipal() {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return new UserPrincipal(123L,
                "Mutush", "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);
    }

}
