package com.example.redditClone.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PostRequest {

    private Long postId;
    private String postTitle;
    private String url;
    private String description;
    private String subredditName;

}
