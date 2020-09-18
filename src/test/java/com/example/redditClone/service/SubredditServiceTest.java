package com.example.redditClone.service;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.SubredditRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SubredditServiceTest {

    @Autowired
    SubredditService subredditService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    SubredditRepository subredditRepository;

    @MockBean
    AuthService authService;

    @Test
    public void shouldMapSubredditObjectToSubredditDTOWhenGetSubredditIsCalled() {
        Subreddit actualSubreddit = Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("What the fuck")
                .creationDate(Instant.now())
                .posts(Arrays.asList(
                        new Post(
                                "Love",
                                "http://127.0.0.1:8000/api/wallet/create",
                                "What a thing",
                                23,
                                Instant.now())))
                .build();

        Mockito.when(subredditRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(actualSubreddit));

        SubredditDTO expectedSubredditDTO = subredditService.getSubreddit(actualSubreddit.getId());

        Assert.assertEquals(expectedSubredditDTO.getName(), expectedSubredditDTO.getName());

    }

    @Test(expected = SubredditNotFoundException.class)
    public void shouldThrowSubredditNotFoundExceptionWhenGetSubredditIsCalledWithNonExistingSubredditID() {
        subredditService.getSubreddit(123l);
        assertThrows(SubredditNotFoundException.class, () -> subredditService.getSubreddit(123l));
    }

    @Test
    public void shouldReturnSubredditWhenSaveIsCalledWithSubredditDTO() {
        // Arrange
        Subreddit actualSubreddit = Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("I love you")
                .creationDate(Instant.now())
                .build();

        Mockito.when(subredditRepository.save(Mockito.any(Subreddit.class))).thenReturn(actualSubreddit);

        Mockito.when(authService.getCurrentUser()).thenReturn(new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        ));

        SubredditDTO subredditDTO = SubredditDTO.builder()
                .name("Love")
                .description("I love you")
                .postCount(12)
                .memberCount(12)
                .build();

        // Act
        SubredditDTO expectedSubredditDTO = subredditService.save(subredditDTO);

        // Assert
        Assert.assertEquals(expectedSubredditDTO.getDescription(), actualSubreddit.getDescription());


    }

    @Test
    public void shouldReturnListOfSubredditDTOsWhenGetAllisCalled() {
        List<Subreddit> actualSubredditList = Arrays.asList(Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("What the fuck")
                .creationDate(Instant.now())
                .posts(Arrays.asList(
                        new Post(
                                "Love",
                                "http://127.0.0.1:8000/api/wallet/create",
                                "What a thing",
                                23,
                                Instant.now())))
                .build());

        Mockito.when(subredditRepository.findAll()).thenReturn(actualSubredditList);

        List<SubredditDTO> expectedSubredditDTOList = subredditService.getAll();

        Assert.assertEquals(expectedSubredditDTOList.size(), 1);
        Assert.assertEquals(expectedSubredditDTOList.get(0).getDescription(),
                actualSubredditList.get(0).getDescription());


    }

}
