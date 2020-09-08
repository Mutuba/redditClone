package com.example.redditClone.models;

import com.example.redditClone.service.UserPrincipal;
import org.junit.Assert;
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
public class CommentTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Post post;
    private Comment comment;

    @Before
    public void setUp() {
        user = new User("Mutuba", "daniel@gmail.com", "TestPassword");
        post = new Post();
        comment = new Comment(123L,
                "I really the current post",
                post, Instant.now(),
                user);

    }

    @Test
    public void testCreateCommentWithAllArgsConstructorArgs() {
        User savedUser = this.entityManager.merge(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);
        comment.setUser(savedUser);
        comment.setPost(savedPost);

        Comment savedComment = this.entityManager.merge(comment);

        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getText()).isEqualTo("I really the current post");
        assertThat(savedComment.getPost().getPostTitle()).isEqualTo(savedPost.getPostTitle());
        assertThat(savedComment.getUser().getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(savedComment.getCreationDate()).isNotNull();
    }


    @Test
    public void testCreateCommentWithSetterMethods() {
        User savedUser = this.entityManager.merge(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);
        Comment comment = new Comment();
        comment.setId(123L);
        comment.setText("It was lovely that you wrote again");
        comment.setUser(savedUser);
        comment.setPost(savedPost);
        comment.setCreationDate(Instant.now());
        Comment savedComment = this.entityManager.merge(comment);

        assertThat(savedComment.getId()).isNotNull();
    }

    @Test
    public void testCreateCommentWithBuilderPattern() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);

        Comment comment = Comment.builder()
                .id(123L)
                .text("It was lovely that you wrote again")
                .post(savedPost)
                .user(savedUser)
                .build();
        Comment savedCommment = this.entityManager.merge(comment);
        assertThat(savedCommment.getId()).isNotNull();
    }


    @Test
    public void testCreateVoteWithBuilderPatternToString() {
        User savedUser = this.entityManager.persistAndFlush(user);

        String stringPost = Comment.builder()
                .id(123L)
                .text("It was lovely that you wrote again")
                .user(savedUser)
                .toString();
        assertThat(stringPost).isNotNull();
    }



    @Test
    public void testEqualsMethodContracts() {

        User savedUser = this.entityManager.persistAndFlush(user);
        Post post = new Post(
                "Love",
                "http://127.0.0.1:8000/api/wallet/create",
                "What a thing",
                23,
                Instant.now());

        Post savedPost = this.entityManager.merge(post);

        Comment comment1 = Comment.builder()
                .id(123L)
                .text("It was lovely that you wrote again")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment comment2 = Comment.builder()
                .id(123L)
                .text("It was lovely that you wrote again")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedCommment1 = this.entityManager.merge(comment1);
        Comment savedCommment2 = this.entityManager.merge(comment2);

        Assert.assertNotEquals(savedCommment1, savedCommment2);

        Assert.assertFalse(savedCommment1.equals(null) || savedCommment1.equals(savedPost));
        Assert.assertTrue(savedCommment1.equals(savedCommment1));

    }

//    @Test
//    public void testHashCodeMethodContracts() {
//        User user1 = new User(
//                "Mutush",
//                "daniel@gmail.com",
//                passwordEncoder.encode("Baraka1234")
//        );
//
//        User user2 = new User(
//                "Mutush1",
//                "daniel1@gmail.com",
//                passwordEncoder.encode("Baraka1234")
//        );
//        userRepository.save(user1);
//        userRepository.save(user2);
//
//        UserPrincipal userPrincipal1 = UserPrincipal.create(user1);
//        UserPrincipal userPrincipal2 = UserPrincipal.create(user2);
//        Assert.assertNotEquals(userPrincipal1.hashCode(), userPrincipal2.hashCode());
//    }
}

