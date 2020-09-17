package com.example.redditClone.models;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SubredditTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Subreddit subreddit;
    private Post post;

    @Before
    public void setUp() {
        user = new User("Mutuba", "daniel@gmail.com", "TestPassword");
        post = new Post();
        subreddit = new Subreddit(123L, "Love",
                "The best thing in the world",
                Arrays.asList(new Post(
                        "Love",
                        "http://127.0.0.1:8000/api/wallet/create",
                        "What a thing",
                        23,
                        Instant.now())), Instant.now(), user);
    }

    @Test
    public void testCreatePostWithAllArgsConstructorArgs() {
        User savedUser = this.entityManager.persistAndFlush(user);
        subreddit.setUser(savedUser);
        Subreddit savedReddit = this.entityManager.merge(subreddit);

        assertThat(savedReddit.getId()).isNotNull();
        assertThat(savedReddit.getDescription()).isNotNull();
        assertThat(savedReddit.getUser().getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(savedReddit.getName()).isEqualTo("Love");

        assertThat(savedReddit.getCreationDate()).isNotNull();
        assertThat(savedReddit.getPosts().size()).isEqualTo(1);


    }


    @Test
    public void testCreatePostWithSetterMethods() {
        Subreddit subreddit = new Subreddit();
        subreddit.setId(123L);
        subreddit.setName("Love");
        subreddit.setDescription("What the fuck");
        subreddit.setPosts(Arrays.asList(new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now())));
        subreddit.setCreationDate(Instant.now());
        Subreddit savedReddit = this.entityManager.merge(subreddit);
        assertThat(savedReddit.getName()).isEqualTo("Love");

    }

    @Test
    public void testCreatePostWithBuilderPattern() {
        User savedUser = this.entityManager.persistAndFlush(user);
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
                .user(savedUser)
                .build();

        Subreddit savedReddit = this.entityManager.merge(subreddit);
        assertThat(savedReddit.getName()).isEqualTo("Love");

    }


    @Test
    public void testCreatePostWithBuilderPatternToString() {
        User savedUser = this.entityManager.persistAndFlush(user);
        String stringReddit = Subreddit.builder()
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
                .user(savedUser)
                .toString();
        assertThat(stringReddit).isNotNull();

    }
}