package com.example.redditClone.controller;

import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.models.AccountVerificationToken;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.TokenRepository;
import com.example.redditClone.repository.UserRepository;
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

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WebAppConfiguration
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    public AuthControllerTest() {
    }


    @Test
    public void shouldReturnCreatedIfRegistrationRequestisOk() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldRaiseMethodArgumentNotValidExceptionWithBadRequestWhenParamsValidationFails() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("Mutuba");
        registrationRequest.setEmail("daniel@gmail.com");

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("errors").value("password field must not be blank"));

    }


    @Test
    public void userShouldLoginSuccessfullyWhenAccountExists() throws Exception {
        UserPrincipal userPrincipal = createPrincipal();
        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userPrincipal);
        LoginRequest loginRequest = loginRequest();

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("Mutush"));
    }


    @Test
    public void userLoginShouldBeUnsuccessfulWhenWrongAccountDetailsAreUsed() throws Exception {

        UserPrincipal userPrincipal = createPrincipal();
        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userPrincipal);

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
    public void shouldReturnOkWhenActivationTokenIsValid() throws Exception {

        User user = new User("Mutuba", "daniel@gmail.com", "Baraka1234");
        AccountVerificationToken accountVerificationToken = mock(AccountVerificationToken.class);
        accountVerificationToken.setUser(user);
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(accountVerificationToken));

        Mockito.when(accountVerificationToken.getUser()).thenReturn(user);
//        Mockito.when(userRepository.save(user)).thenReturn(user);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/verify/{token}", accountVerificationToken))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("status").value(200));
    }

    @Test
    public void shouldRaiseActivationExceptionWhenActivationTokenIsInvalid() throws Exception {

        String token = authToken();
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/verify/{token}", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("error").value("Invalid Activation Token"));
    }



    // Utility functions used in the test class

    /**
     * Return registrationRequest .
     *
     * @return The registrationRequest object.
     */


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


    public String activationToken() {
        String token = "wqerwtytyjukilroli7ruktyrtrbrntj";
        return token;
    }


    public UserPrincipal createPrincipal() {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        return new UserPrincipal(123L,
                "Mutush", "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);
    }

}
