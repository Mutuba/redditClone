package com.example.redditClone.models;

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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class NotificationEmailTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NotificationEmail notificationEmail;

    @Before
    public void setUp() {
        notificationEmail = new NotificationEmail(
                "Account Activation",
                "daniel@gmail.com",
                "Thanks for joining subreddit");
    }


    @Test
    public void testCreateCommentWithAllArgsConstructorArgs() {
        NotificationEmail savedNotificationEmail = this.entityManager.persistAndFlush(notificationEmail);

        assertThat(savedNotificationEmail.getRecepient()).isEqualTo("daniel@gmail.com");
        assertThat(savedNotificationEmail.getSubject()).isEqualTo("Account Activation");
        assertThat(savedNotificationEmail.getBody()).isEqualTo("Thanks for joining subreddit");
    }

//    @Test
//    public void testEqualsMethodContracts() {
//        RefreshToken refreshToken1  = new RefreshToken(12L, "simpletoken1", Instant.now());
//        RefreshToken savedRefreshToken1 = this.entityManager.merge(refreshToken);
//
//        RefreshToken savedRefreshToken2 = this.entityManager.merge(refreshToken1);
//
//        Assert.assertNotEquals(savedRefreshToken1, savedRefreshToken2);
//
//        Assert.assertFalse(savedRefreshToken2.equals(null) || savedRefreshToken2.equals(User.class));
//        Assert.assertTrue(savedRefreshToken2.equals(savedRefreshToken2));
//        Assert.assertTrue(savedRefreshToken2.getId().equals(savedRefreshToken2.getId()));
//
//    }
//
//    @Test
//    public void testHashCodeMethodContracts() {
//
//        RefreshToken savedRefreshToken1 = this.entityManager.merge(refreshToken);
//        int hashCode = savedRefreshToken1.hashCode();
//        Assert.assertNotNull(hashCode);
//    }
//
//    @Test
//    public void testCreateVoteWithBuilderPatternToString() {
//        RefreshToken savedRefreshToken = this.entityManager.merge(refreshToken);
//
//        String stringPost = savedRefreshToken.toString();
//        assertThat(stringPost).isNotNull();
//    }


}


