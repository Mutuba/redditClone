package com.example.redditClone.repository;

import com.example.redditClone.models.Post;
import com.example.redditClone.models.User;
import com.example.redditClone.models.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
