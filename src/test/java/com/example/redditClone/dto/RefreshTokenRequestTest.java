package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class RefreshTokenRequestTest extends TestCase {

    @Test
    public void testBuilderPattern(){
        String refreshTokenRequest = RefreshTokenRequest.builder()
                .username("Mutuba")
                .refreshToken("testfreshtoken").toString();

        Assert.assertTrue(refreshTokenRequest instanceof String);
    }

    @Test
    public void testSetterMethods(){
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("SampleToken");
        refreshTokenRequest.setUsername("Mutuba");

    }
}
