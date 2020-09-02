package com.example.redditClone.controller;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.security.JwtTokenProvider;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static groovy.json.JsonOutput.toJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SubredditControllerTest {

    // Used for converting heroes to/from JSON
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SubredditService subredditService;

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

    @Before
    public void authSetUp() {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        UserPrincipal userPrincipal = new UserPrincipal(123L,
                "Mutush", "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);
        Mockito.when(customUserDetailsService.loadUserById(123L)).thenReturn(userPrincipal);
        Mockito.when(jwtTokenProvider.validateToken(Mockito.anyString())).thenReturn(Boolean.TRUE);
    }


    @Test
    public void addSubreddit_ShouldReturn_Created_Subreddit() throws Exception {
        String token = authToken();

        SubredditDTO subredditDTO = SubredditDTO.builder().name("Love").description(
                "What the fuck"
        ).build();

        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(123L);
        Mockito.when(subredditService.save(Mockito.any(SubredditDTO.class))).thenReturn(subredditDTO);

        // Act & Assert

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddit")
                .header("Authorization", "Bearer " + token)
                .content(toJson(subredditDTO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Love"))
                .andExpect(jsonPath("$.description").value("What the fuck"));
    }


    @Test
    public void getAllSubreddits_ShouldReturn_List_of_Subreddits() throws Exception {
        String token = authToken();
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(123L);

        List<SubredditDTO> subredditDTOS = Arrays.asList(
                SubredditDTO.builder().name("iPhone12").description("The best smartphone ever").build(),
                SubredditDTO.builder().name("iPhone13").description("The best smartphone ever").build(),
                SubredditDTO.builder().name("iPhone14").description("The best smartphone ever").build()
        );

        Mockito.when(subredditService.getAll()).thenReturn(subredditDTOS);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    public void getSubreddit_ShouldReturn_Found_Subreddit() throws Exception {
        String token = authToken();
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(123L);

        SubredditDTO subredditDTO =
                SubredditDTO.builder().name("iPhone12").description("The best smartphone ever").id(123L).build();


        Mockito.when(subredditService.getSubreddit(123l)).thenReturn(subredditDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit/123").header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("iPhone12"))
                .andExpect(jsonPath("$.description").value("The best smartphone ever"));
    }


    @Test
    public void getSubreddit_ShouldReturn_404_Not_Found_For_Non_Existent_Subreddit() throws Exception {
        String token = authToken();
        Mockito.when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(123L);

        Mockito.when(subredditService.getSubreddit(Mockito.anyLong()))
                .thenThrow(new SubredditNotFoundException("Subreddit not found with id -" + Mockito.anyLong()));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/subreddit/123").header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
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

    /**
     * Convert JSON Result to object.
     *
     * @param result The contents
     * @param tClass The expected object class
     * @return The result as class.
     * @throws Exception if you got any of the above wrong.
     */
    <T> T fromJsonResult(MvcResult result, Class<T> tClass) throws Exception {
        return this.mapper.readValue(result.getResponse().getContentAsString(), tClass);
    }
}
