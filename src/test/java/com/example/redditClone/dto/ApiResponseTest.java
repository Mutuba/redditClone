package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Before;

public class ApiResponseTest extends TestCase {

    private APIResponse apiResponse;

    @Before
    public void setUp() {
        apiResponse = new APIResponse();
    }


    public void testSettterMethods() {
        apiResponse.setMessage("Test message for API response");
        apiResponse.setSuccess(true);

        assertTrue(apiResponse.getSuccess());

    }
}
