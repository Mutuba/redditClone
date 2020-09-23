package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class PostResponseTest extends TestCase {

    private PostResponse postResponse;

    public void setUp(){
        postResponse = new PostResponse();
    }

    @Test
    public void testPostRequestsetterMethods(){
        postResponse.setPostId(12l);
        postResponse.setDescription("The best that ever was");
        postResponse.setPostTitle("Mulan");
        postResponse.setSubredditName("Mulan the new Game of Thrones");
        postResponse.setUrl("http://localhost:5000/api/auth//user/me");
        postResponse.setUserName("Mutuba");
        postResponse.setVoteCount(12);
        postResponse.setCommentCount(12);
        postResponse.setDuration("just now");
        postResponse.setUpVote(true);
        postResponse.setDownVote(false);

        Assert.assertTrue(postResponse instanceof PostResponse);

        Assert.assertEquals("The best that ever was", postResponse.getDescription());
    }

    @Test
    public void testPostResponseBuilderPatternToStringMethod() {
        String postResponseString = PostResponse.builder()
                .postId(12l)
                .description("I am the best")
                .toString();
        Assert.assertTrue(postResponseString instanceof String);
    }

}