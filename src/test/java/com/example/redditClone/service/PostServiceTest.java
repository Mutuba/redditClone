package com.example.redditClone.service;

import com.example.redditClone.dto.PostResponse;
import com.example.redditClone.models.*;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.VoteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
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

    @MockBean
    PostRepository postRepository;


    @Test
    public void shouldMapSubredditObjectToSubredditDTOWhenGetSubredditIsCalled() {

        // Given
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

        Mockito.when(customUserDetailsService.loadUserById(Mockito.anyLong())).thenReturn(userPrincipal);

        Mockito.when(authService.isLoggedIn()).thenReturn(true);
        Mockito.when(authService.getCurrentUser()).thenReturn(user);

        Mockito.when(voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                post, user)).thenReturn(Optional.of(vote));

        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));

        // When
        PostResponse postResponse=  postService.getPost(post.getPostId());

        //Assert
        Assert.assertEquals(postResponse.getPostTitle(), post.getPostTitle());
        Assert.assertTrue(postResponse.isUpVote());

    }


//    //Helper function

    public UserPrincipal createPrincipal() {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        return new UserPrincipal(123L,
                "Mutush", "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);

    }

}
