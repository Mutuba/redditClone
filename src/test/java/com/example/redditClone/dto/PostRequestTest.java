package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class PostRequestTest extends TestCase {

    private PostRequest postRequest;

    public void setUp(){
        postRequest = new PostRequest();
    }

    @Test
    public void testPostRequestsetterMethods(){
        postRequest.setPostId(12l);
        postRequest.setDescription("The best that ever was");
        postRequest.setPostTitle("Mulan");
        postRequest.setSubredditName("Mulan the new Game of Thrones");
        postRequest.setUrl("http://localhost:5000/api/auth//user/me");

        Assert.assertTrue(postRequest instanceof PostRequest);
    }


    @Test
    public void testPostRequestBuilderPatternToStringMethod(){
        String postRequestString = PostRequest.builder().postId(123l).postTitle("Love").toString();

        Assert.assertTrue(postRequestString instanceof String);
    }
}
