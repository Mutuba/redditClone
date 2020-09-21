package com.example.redditClone.dto;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

public class SubredditDTOTest extends TestCase {

    private SubredditDTO subredditDTO;

    @Before
    public void setUp() {
        subredditDTO = SubredditDTO.builder().id(12l).build();
    }

    @Test
    public void testSubredditDTOSetterMethods() {
        subredditDTO.setId(12l);
        subredditDTO.setName("Love Letter");
        subredditDTO.setDescription("A messed up r/ship");
        subredditDTO.setMemberCount(12);
        subredditDTO.setPostCount(3);
        Assert.assertNotNull(subredditDTO.getDescription());
    }


    @Test
    public void testSubredditDTOBuilderPatternToStringMethod() {
        String subredditDTOString = SubredditDTO.builder()
                .id(12l)
                .description("I am the best")
                .name("Java")
                .toString();
        Assert.assertTrue(subredditDTOString instanceof String);
    }

}
