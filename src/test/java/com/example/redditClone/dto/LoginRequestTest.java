package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginRequestTest extends TestCase {
    LoginRequest loginRequest;

    @Before
    public void setUp(){

        loginRequest = new LoginRequest();
    }

    @Test
    public void testSetterMethds(){
        loginRequest.setUsername("mutuba");
        loginRequest.setPassword("Baraka");

        Assert.assertTrue(loginRequest instanceof LoginRequest);
    }
}

