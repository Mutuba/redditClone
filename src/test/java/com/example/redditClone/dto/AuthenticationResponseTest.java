package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;
import java.time.Instant;

public class AuthenticationResponseTest extends TestCase {

    private AuthenticationResponse authenticationResponse;

    @Before
    public void setUp() {
        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    public void testSetterMethods(){
        authenticationResponse.setAuthenticationToken("testAuthenticationTokne");
        authenticationResponse.setRefreshToken("testRefreshToken");
        authenticationResponse.setExpiresAt(Instant.ofEpochSecond(1550000002));
        authenticationResponse.setUsername("Mutuba");
        Assert.assertNotNull(authenticationResponse.getAuthenticationToken());
    }

    @Test
    public void testBuilderPatternToStringMethod(){
        String authenticationResponseString = AuthenticationResponse.builder()
                .authenticationToken("SampleToken")
                .username("Mutuba")
                .toString();

        Assert.assertTrue(authenticationResponseString instanceof String);
    }
}
