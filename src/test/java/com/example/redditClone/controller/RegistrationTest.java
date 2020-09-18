package com.example.redditClone.controller;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @Test
    public void userSignUpShouldFailIfUsernameIsTaken() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

        Mockito.when(userRepository.existsByUsername(registrationRequest.getUsername()))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void userSignUpShouldFailIfEmailIsTaken() throws Exception {
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


    // helper functions
    public RegistrationRequest registrationRequest() {
        return new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

    }

}
