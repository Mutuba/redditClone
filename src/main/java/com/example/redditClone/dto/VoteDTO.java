package com.example.redditClone.dto;

import com.example.redditClone.models.VoteType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
    private VoteType voteType;
    private Long postId;
}
