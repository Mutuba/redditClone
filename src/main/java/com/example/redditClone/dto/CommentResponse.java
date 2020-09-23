package com.example.redditClone.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private Long postId;
    private String creationDate;
    private String userName;
}
