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
public class PostTest {

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
        Subreddit savedReddit = this.entityManager.merge(subreddit);
        Post post = new Post(123L,
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,savedUser,
                Instant.now(), savedReddit);

        Post savedPost = this.entityManager.merge(post);
        assertThat(savedPost.getDescription()).isNotNull();
        assertThat(savedPost.getSubreddit().getDescription()).isEqualTo(savedReddit.getDescription());
        assertThat(savedPost.getDescription()).isEqualTo("What a thing");
        assertThat(savedPost.getCreationDate()).isNotNull();
        assertThat(savedPost.getUser().getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(savedPost.getVoteCount()).isEqualTo(23);
        assertThat(savedPost.getUrl()).isEqualTo("http://127.0.0.1:8000/api/wallet/create");
        assertThat(savedPost.getPostTitle()).isEqualTo("Love");
        assertThat(savedPost.getPostId()).isNotNull();

    }


    @Test
    public void testCreatePostWithSetterMethods() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Subreddit savedReddit = this.entityManager.merge(subreddit);
        Post post = new Post();
        post.setPostId(123L);
        post.setPostTitle("Love");
        post.setUrl("http://127.0.0.1:8000/api/wallet/create");
        post.setDescription("What the fuck");
        post.setVoteCount(12);
        post.setUser(savedUser);
        post.setCreationDate(Instant.now());
        post.setSubreddit(savedReddit);
        Post savedPost = this.entityManager.merge(post);
        assertThat(savedPost.getPostId()).isNotNull();

    }

    @Test
    public void testCreatePostWithBuilderPattern() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Subreddit savedReddit = this.entityManager.merge(subreddit);
        Post post = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("What the fuck")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(savedUser)
                .subreddit(savedReddit)
                .build();
        Post savedPost = this.entityManager.merge(post);
        assertThat(savedPost.getPostId()).isNotNull();

    }



    @Test
    public void testCreatePostWithBuilderPatternToString() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Subreddit savedReddit = this.entityManager.merge(subreddit);
        String stringPost = Post.builder()
                .postId(123L)
                .postTitle("Love")
                .description("What the fuck")
                .url("http://127.0.0.1:8000/api/wallet/create")
                .voteCount(12)
                .creationDate(Instant.now())
                .user(savedUser)
                .subreddit(savedReddit)
                .toString();
        assertThat(stringPost).isNotNull();

    }
}

