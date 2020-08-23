package com.example.redditClone.repository;

import com.example.redditClone.models.Subreddit;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubredditRepository extends CrudRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}
