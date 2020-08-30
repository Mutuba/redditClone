package com.example.redditClone;

import com.example.redditClone.dto.APIResponse;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.dto.SubredditDTO;
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

    // Used for converting heroes to/from JSON
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthService authService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JavaMailSender sender;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
//
//    @Before
//    public void authSetUp(){
//        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
//                new SimpleGrantedAuthority("USER")
//        );
//
//        UserPrincipal userPrincipal = new UserPrincipal(123L,
//                "Mutush", "daniel@gmail.com",
//                passwordEncoder.encode("Baraka1234"), grantedAuthority);
//        when(customUserDetailsService.loadUserById(123L)).thenReturn(userPrincipal);
//        when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);
//    }



    @Test
    public void userSigningUpReturnsCreated() throws Exception {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");


//        doNothing().when(authService).register(any(RegistrationRequest.class));

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

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(true);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .content(toJson(registrationRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }




    /**
     * Return registrationRequest .
     *
     * @return The registrationRequest object.
     *
     */

    public RegistrationRequest registrationRequest(){
        return new RegistrationRequest(
                "Course1", "daniel@gmail.com", "Baraka1234");

    }

    /**
     * Return an Auth Token.
     *
     * @return The result as a string.
     *
     * @throws Exception
     *             if you got any of the above wrong.
     */


    public String authToken(){

        String token = "wqerwtytyjukilroli7ruktyrtrbrntj";
        return token;
    }

}
