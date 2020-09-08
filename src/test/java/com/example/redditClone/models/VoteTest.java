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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class VoteTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Post post;
    private Vote vote;
    private VoteType voteType;

    @Before
    public void setUp() {
        user = new User("Mutuba", "daniel@gmail.com", "TestPassword");
        post = new Post();
        voteType = VoteType.UPVOTE;
        vote = new Vote(123L, voteType, post, user);

    }

    @Test
    public void testCreatePostWithAllArgsConstructorArgs() {
        User savedUser = this.entityManager.merge(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);
        vote.setUser(savedUser);
        vote.setPost(savedPost);

        Vote savedVote = this.entityManager.merge(vote);
        assertThat(savedVote.getVoteId()).isNotNull();
        assertThat(savedVote.getVoteType()).isEqualTo(VoteType.UPVOTE);
        assertThat(savedVote.getPost().getPostTitle()).isEqualTo(savedPost.getPostTitle());
        assertThat(savedVote.getUser().getEmail()).isEqualTo(savedUser.getEmail());

    }


    @Test
    public void testCreateVoteWithSetterMethods() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);
        vote.setUser(savedUser);
        vote.setPost(savedPost);
        vote.setVoteType(VoteType.UPVOTE);
        vote.setVoteId(123L);
        Vote savedVote = this.entityManager.merge(vote);
        assertThat(savedVote.getVoteId()).isNotNull();

    }

    @Test
    public void testCreatePostWithBuilderPattern() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);

        Vote vote = Vote.builder()
                .voteId(123L)
                .voteType(VoteType.UPVOTE)
                .post(savedPost)
                .user(savedUser)
                .build();
        Vote savedVote = this.entityManager.merge(vote);
        assertThat(savedVote.getVoteId()).isNotNull();

    }


    @Test
    public void testCreateVoteWithBuilderPatternToString() {
        User savedUser = this.entityManager.persistAndFlush(user);

        String stringPost = Vote.builder()
                .voteId(123L)
                .user(savedUser)
                .toString();
        assertThat(stringPost).isNotNull();

    }
}

