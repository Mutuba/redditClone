package com.example.redditClone.controller;

import com.example.redditClone.dto.RefreshTokenRequest;
import com.example.redditClone.models.RefreshToken;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.RefreshTokenRepository;
import com.example.redditClone.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Optional;
import java.util.UUID;

import static groovy.json.JsonOutput.toJson;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class AuthControllerRefreshTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @Test
    public void userShouldRefreshTokenWhenRefreshTokenIsRequestedWithValidTokenAndUsername()
            throws Exception {
        User user = createUser();
        String token = authToken();
        String refreshToken = refreshToken();

        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(new RefreshToken()));

        when(jwtTokenProvider.generateTokenWithUserName(user.getUsername())).thenReturn(token);

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken, "Mutuba");

        //Act & Assert
        String uri = "/api/auth/refresh/token/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(toJson(refreshTokenRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("Mutuba"))
                .andExpect(jsonPath("$.authenticationToken").exists());
    }


    @Test
    public void shouldReturnTokenDeletedMessageWhenUserLogsOutOfTheSystem()
            throws Exception {
        String refreshToken = refreshToken();

        doNothing().when(refreshTokenRepository).deleteByToken(anyString());
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken, "Mutuba");

        //Act & Assert
        String uri = "/api/auth/logout/";
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(toJson(refreshTokenRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value("Refresh Token Deleted Successfully!!"));
    }

    // Utility functions used in the test class

    public String authToken() {
        return UUID.fromString("00000000-000-0000-0000-000000000001").toString();
    }

    public String refreshToken() {
        return UUID.fromString("00000000-000-0000-0000-000000000001").toString();
    }

    public User createUser() {
        return new User("Mutuba", "daniel@gmail.com", "Baraka1234");
    }


}
