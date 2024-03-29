package com.example.redditClone.models;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
public class Post {
    @Id
    @SequenceGenerator(name = "POST_GEN", sequenceName = "SEQ_POST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_GEN")
    private Long postId;
    @NotBlank(message = "Post Title is required")
    private String postTitle;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    private Instant creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")

    // a sub reddit owns a post and one can have many posts that are either upvoted or downvoted
    // hence a post has many votes
    private Subreddit subreddit;

    public Post(@NotBlank(message = "Post Title is required") String postTitle,
                @Nullable String url,
                @Nullable String description,
                Integer voteCount,
                Instant creationDate) {
        this.postTitle = postTitle;
        this.url = url;
        this.description = description;
        this.voteCount = voteCount;
        this.creationDate = creationDate;
    }
}
