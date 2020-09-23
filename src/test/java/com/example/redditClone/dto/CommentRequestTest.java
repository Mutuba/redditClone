package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CommentRequestTest extends TestCase {

    private CommentRequest commentRequest;

    public void setUp(){
        commentRequest = new CommentRequest();
    }


    @Test
    public void testCommentResponseSetterMethods(){
        commentRequest.setId(123l);
        commentRequest.setText("Love");


        Assert.assertTrue(commentRequest instanceof CommentRequest);

        Assert.assertEquals("Love", commentRequest.getText());
    }

    @Test
    public void testCommentRequestBuilderToStringMethod(){

        String commentRequestString = CommentRequest
                .builder().id(12l).text("nice one").toString();

        Assert.assertTrue(commentRequestString instanceof String);

    }
}
