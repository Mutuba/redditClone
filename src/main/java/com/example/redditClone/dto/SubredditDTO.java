package com.example.redditClone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubredditDTO {
    private Long id;
    private String name;
    private String description;
    private Integer postCount;
    private Integer memberCount;
}

