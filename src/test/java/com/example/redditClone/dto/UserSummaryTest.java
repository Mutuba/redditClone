package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Test;
import org.testng.Assert;

public class UserSummaryTest extends TestCase {

    @Test
    public void testUserSummarySetterMethods(){
        UserSummary userSummary = new UserSummary();
        userSummary.setEmail("daniel@gmail.com");
        userSummary.setUsername("Mutuba");
        userSummary.setId(123l);
        Assert.assertNotNull(userSummary);

    }

}
