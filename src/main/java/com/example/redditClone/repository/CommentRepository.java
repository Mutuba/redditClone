package com.example.redditClone.repository;

import com.example.redditClone.models.Comment;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
