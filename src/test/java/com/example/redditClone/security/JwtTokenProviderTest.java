package com.example.redditClone.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private String token;

    @Value("${app.jwtSecret}")
    private String jwtSecret;


    private int jwtExpirationInMs = 999999999;

    Date now = new Date();

    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    @Before
    public void setUp() {
        token = Jwts.builder()
                .setSubject(Long.toString(1234l))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    @Test
    public void shouldGetUserIdFromTokenWhenValidJwtToken() {
        Assert.assertNotNull(jwtTokenProvider.getUserIdFromJWT(token));
    }


    @Test
    public void shouldReturnTrueWhenTokenIsValid() {
        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }


    @Test()
    public void shouldThrowExpiredJwtExceptionWhenTokenHasExpired() {
        token = Jwts.builder()
                .setSubject(Long.toString(1234l))
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        Assert.assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test()
    public void shouldThrowInvalidSignatureExceptionWhenInvalidSignature() {
        token = Jwts.builder()
                .setSubject(Long.toString(1234l))
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, "jwtSecretofmine")
                .compact();
        Assert.assertFalse(jwtTokenProvider.validateToken(token));
    }


    @Test()
    public void shouldThrowMalformedExceptionWhenInvalidSignature() {
        Assert.assertFalse(jwtTokenProvider.validateToken("blah"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenInvalidSignature() throws Exception {
        token = Jwts.builder()
                .setSubject(Long.toString(1234l))
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, "")
                .compact();
        Assert.assertFalse(jwtTokenProvider.validateToken(token));
        Assert.assertThrows(IllegalArgumentException.class, ()->jwtTokenProvider.validateToken(token) );
        throw new IllegalArgumentException("JWT claims string is empty.");
    }

}

