package com.example.redditClone.dto;

import com.example.redditClone.models.User;
import junit.framework.TestCase;
import org.junit.Before;

public class ApiResponseTest extends TestCase {

    private APIResponse apiResponse;

    @Before
    public void setUp() {
        apiResponse = new APIResponse(
                true,
                "Test api resonse"
        );
    }


    public void testSettterMethods() {

    }
}
