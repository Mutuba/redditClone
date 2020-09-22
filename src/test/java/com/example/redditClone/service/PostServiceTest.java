package com.example.redditClone.service;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.models.*;
import com.example.redditClone.repository.SubredditRepository;
import com.example.redditClone.repository.VoteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    VoteRepository voteRepository;

    @MockBean
    AuthService authService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    // current user, post, return true or false
    // mock of voterepo

    @Test
    public void shouldMapSubredditObjectToSubredditDTOWhenGetSubredditIsCalled() {
        User user = new User("Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"));
        UserPrincipal userPrincipal = createPrincipal();

        Post post = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("I love you")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(user)
                .subreddit(Subreddit
                        .builder()
                        .id(123L)
                        .name("Love")
                        .description("I love you")
                        .creationDate(Instant.now())
                        .build())
                .build();


        Vote vote = Vote.builder().voteId(123l)
                .voteType(VoteType.UPVOTE).post(post).user(user).build();
        Mockito.when(voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                post, user)).thenReturn(Optional.of(vote));
        Mockito.when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);

        Mockito.when(authService.getCurrentUser()).thenReturn(user);


        Assert.assertEquals(expectedSubredditDTO.getName(), expectedSubredditDTO.getName());

    }
//
//    @Test(expected = SubredditNotFoundException.class)
//    public void shouldThrowSubredditNotFoundExceptionWhenGetSubredditIsCalledWithNonExistingSubredditID() {
//        subredditService.getSubreddit(123l);
//        assertThrows(SubredditNotFoundException.class, () -> subredditService.getSubreddit(123l));
//    }
//
//
//    //Helper function
//
//    public UserPrincipal createPrincipal() {
//        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
//                new SimpleGrantedAuthority("ROLE_USER")
//        );
//        return new UserPrincipal(123L,
//                "Mutush", "daniel@gmail.com",
//                passwordEncoder.encode("Baraka1234"), grantedAuthority);
//
//    }

}
