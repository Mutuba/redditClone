
package com.example.redditClone.service;

import com.example.redditClone.models.User;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class UserPrincipalTest extends TestCase {

    private User user;

    @Before
    public void setUp() {
        user = new User(
                "Mutuba01",
                "mutuba@gmail.com",
                "Thanks for joining subreddit");
    }

    @Test
    public void testCreateUserPrincipal() {

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Assert.assertNotNull(userPrincipal.getAuthorities());
        Assert.assertNotNull(userPrincipal.getUsername());
        Assert.assertEquals(user.getUsername(), userPrincipal.getUsername());
    }


    @Test
    public void testEqualsMethodContracts() {
        UserPrincipal userPrincipal1 = UserPrincipal.create(user);
        UserPrincipal userPrincipal2 = UserPrincipal.create(new User(
                "Ashah",
                "mutuba@gmail.com",
                "Baraka1234"));
        UserPrincipal userPrincipal3 = UserPrincipal.create(new User(
                "Ashah",
                "daniel@gmail.com",
                "Baraka1234"));

        // assert this == o (same reference or identity)
        Assert.assertEquals(userPrincipal1, userPrincipal1);

        // assert different identities but same values
        Assert.assertEquals(userPrincipal1, UserPrincipal.create(user));

        // based on different class
        Assert.assertNotEquals(userPrincipal1, User.class);
        // null check
        Assert.assertNotEquals(userPrincipal1, null);

        // assert false based on username for equality
        Assert.assertNotEquals(userPrincipal1, userPrincipal2);

        // based on different email
        Assert.assertNotEquals(userPrincipal2, userPrincipal3);


    }

    @Test
    public void testHashCodeMethodContact(){
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        int hashCode = userPrincipal.hashCode();

        Assert.assertEquals(userPrincipal.hashCode(), hashCode);

    }
}
