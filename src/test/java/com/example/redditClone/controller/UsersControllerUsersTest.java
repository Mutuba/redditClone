package com.example.redditClone.controller;

import com.example.redditClone.security.JwtTokenProvider;
import com.example.redditClone.service.AuthService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsersControllerUsersTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    AuthService authService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;


    @Test
    public void shouldReturnCurrentUserDetails() throws Exception{
        UserPrincipal userPrincipal = createPrincipal();
        String token = authToken();
        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(userPrincipal.getId());

        Mockito.when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/auth/user/me").header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("email").value("daniel@gmail.com"))
                .andExpect(status().isOk());

    }



    // Helper function

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
}
