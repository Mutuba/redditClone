package com.example.redditClone.controller;

import com.example.redditClone.dto.AuthenticationResponse;
import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static groovy.json.JsonOutput.toJson;
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
        Mockito.when(authService.login(loginRequest)).thenReturn(AuthenticationResponse.builder()
                .authenticationToken("qdfwdegrbhneb")
                .refreshToken("wertvv")
                .username(loginRequest.getUsername())
                .build());

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .content(toJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("Mutush"));
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
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }


    @Test
    public void userVerifyTokenIsSuccessful() throws Exception {
        String token = authToken();
        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/verify/token")
                .content(toJson(token))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
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
