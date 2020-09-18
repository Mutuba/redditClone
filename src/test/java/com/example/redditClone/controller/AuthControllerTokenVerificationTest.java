package com.example.redditClone.controller;


import com.example.redditClone.models.AccountVerificationToken;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class AuthControllerTokenVerificationTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TokenRepository tokenRepository;



    @Test
    public void shouldReturnOkWhenActivationTokenIsValid() throws Exception {

        User user = new User("Mutuba", "daniel@gmail.com", "Baraka1234");
        AccountVerificationToken accountVerificationToken = mock(AccountVerificationToken.class);
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(accountVerificationToken));

        Mockito.when(accountVerificationToken.getUser()).thenReturn(user);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/verify/{token}", accountVerificationToken))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRaiseActivationExceptionWhenActivationTokenIsInvalid() throws Exception {

        String token = activationToken();
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/verify/{token}", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("error").value("Invalid Activation Token"));
    }



    // Utility function used in the test class

    /**
     * Return token .
     */

    public String activationToken() {
        String token = "wqerwtytyjukilroli7ruktyrtrbrntj";
        return token;
    }



}
