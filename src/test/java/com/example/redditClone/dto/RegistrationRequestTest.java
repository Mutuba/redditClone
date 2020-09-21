package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;

public class RegistrationRequestTest extends TestCase {
    RegistrationRequest registrationRequest;

    public void setUp(){

        registrationRequest = new RegistrationRequest();
    }

    public void testSetterMethds(){
        registrationRequest.setEmail("Daniel@gmail.com");
        registrationRequest.setUsername("Mutuba");
        registrationRequest.setPassword("Baraka");

        Assert.assertTrue(registrationRequest instanceof RegistrationRequest);
    }
}
