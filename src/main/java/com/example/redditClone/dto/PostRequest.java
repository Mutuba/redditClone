package com.example.redditClone.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PostRequest {
    private Long postId;
    @NotBlank
    private String postTitle;

    private String url;
    private String description;
    private String subredditName;

}
