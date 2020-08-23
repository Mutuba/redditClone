package com.example.redditClone.repository;

import com.example.redditClone.models.Post;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}