package com.example.redditClone.dto;

import com.example.redditClone.models.Post;
import com.example.redditClone.models.VoteType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class VoteDTOTest extends TestCase {

    private VoteDTO voteDTO;
    private Post post;

    public void setUp() {
        post = new Post();
        voteDTO = new VoteDTO(VoteType.UPVOTE, post.getPostId());

    }

    @Test
    public void testVoteDTOAttributes(){
        Assert.assertTrue(voteDTO instanceof VoteDTO);
        Assert.assertEquals(VoteType.UPVOTE, voteDTO.getVoteType());
    }
}
