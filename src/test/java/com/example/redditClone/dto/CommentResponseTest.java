package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CommentResponseTest extends TestCase {

    private CommentResponse commentResponse;

    public void setUp(){
        commentResponse = new CommentResponse();
    }


    @Test
    public void testCommentResponseSetterMethods(){
        commentResponse.setId(123l);
        commentResponse.setCreationDate("now");
        commentResponse.setText("Love you");
        commentResponse.setPostId(12l);
        commentResponse.setUserName("Mutuba");


        Assert.assertTrue(commentResponse instanceof CommentResponse);

        Assert.assertEquals("Mutuba", commentResponse.getUserName());
    }

    @Test
    public void testCommentResponseBuilderToStringMethod(){

        String commentResponseString = CommentResponse.builder().id(12l).text("nice one").toString();

        Assert.assertTrue(commentResponseString instanceof String);

    }
}
