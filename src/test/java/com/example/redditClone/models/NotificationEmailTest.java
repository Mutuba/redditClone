package com.example.redditClone.models;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationEmailTest extends TestCase {

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
        assertThat(notificationEmail.getRecepient()).isEqualTo("daniel@gmail.com");
        assertThat(notificationEmail.getSubject()).isEqualTo("Account Activation");
        assertThat(notificationEmail.getBody()).isEqualTo("Thanks for joining subreddit");
    }


    @Test
    public void testCreateNotificationEmailUsingSetterMethod() {
        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject("New Account Creation Notification");
        notificationEmail.setRecepient("daniel@gmail.com");
        notificationEmail.setBody("Thank you for creating an account with us. We look forward to awesome articles");
        assertThat(notificationEmail.getRecepient()).isEqualTo("daniel@gmail.com");

    }


    @Test
    public void testEqualsMethodContracts() {
        NotificationEmail mutubaNotificationEmail = new NotificationEmail("Account Activation",
                "mutuba@gmail.com",
                "Thanks for joining subreddit");

        NotificationEmail danNotificationEmail = new NotificationEmail("Account Activation",
                "daniel@gmail.com",
                "Thanks for joining subreddit");

        // null check
        Assert.assertNotEquals (mutubaNotificationEmail, null);

        // same object (this == o) i.e same identity
        Assert.assertEquals (mutubaNotificationEmail, mutubaNotificationEmail);

        // different class assertion
        Assert.assertNotEquals(mutubaNotificationEmail, User.class);

        // passed in class has same attributes as the o, i.e equivalence based on value not identity
        Assert.assertEquals(mutubaNotificationEmail, new NotificationEmail(
                "Account Activation",
                "mutuba@gmail.com",
                "Thanks for joining subreddit"));

        // passed in class has different attributes as the o, i.e equivalence based on value not identity
        // to assert the && case (different body fails equality check)
        Assert.assertNotEquals(mutubaNotificationEmail, new NotificationEmail("Account Activation",
                "mutuba@gmail.com",
                "Thanks for joining subreddit2"));

        // assert different subjects fails equality check
        Assert.assertNotEquals(mutubaNotificationEmail, danNotificationEmail);

    }


    @Test
    public void testCreateVoteWithBuilderPatternToString() {
        String stringPost = notificationEmail.toString();
        assertThat(stringPost).isNotNull();
    }


}


