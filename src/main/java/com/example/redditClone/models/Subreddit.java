package com.example.redditClone.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@Entity
public class Subreddit {
    @Id
    @SequenceGenerator(name = "SUB_GEN", sequenceName = "SEQ_SUB", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUB_GEN")
    private Long id;
    @NotBlank(message = "Subreddit name is required")
    private String name;
    @NotBlank(message = "Subreddit description is required")
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;
    private Instant creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
