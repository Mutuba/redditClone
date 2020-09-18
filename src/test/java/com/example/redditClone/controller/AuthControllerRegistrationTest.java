package com.example.redditClone.controller;

import com.example.redditClone.dto.RegistrationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerRegistrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldReturnCreatedIfRegistrationRequestIsOk() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "MutubaDan", "daniel14@gmail.com", "Baraka1234");

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

}
