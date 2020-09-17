package com.example.redditClone.service;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.repository.SubredditRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SubredditServiceTest {
    @Autowired
    SubredditService subredditService;

    @MockBean
    SubredditRepository subredditRepository;

    @Test
    public void shouldMapSubredditObjectToSubredditDTO() {
        Subreddit subreddit = Subreddit.builder()
                .id(123L)
                .name("Love")
                .description("What the fuck")
                .creationDate(Instant.now())
                .posts(Arrays.asList(new Post(
                        "Love",
                        "http://127.0.0.1:8000/api/wallet/create",
                        "What a thing",
                        23,
                        Instant.now())))
                .build();

        Mockito.when(subredditRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(subreddit));

        SubredditDTO subredditDTO = subredditService.getSubreddit(subreddit.getId());

        Assert.assertEquals("Love", subredditDTO.getName());



    }


}
