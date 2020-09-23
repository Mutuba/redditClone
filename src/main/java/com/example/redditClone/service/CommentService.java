package com.example.redditClone.service;

import com.example.redditClone.dto.CommentRequest;
import com.example.redditClone.dto.CommentResponse;
import com.example.redditClone.exception.PostNotFoundException;
import com.example.redditClone.exception.UserNotFoundException;
import com.example.redditClone.models.Comment;
import com.example.redditClone.models.NotificationEmail;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.CommentRepository;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.UserRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final MailBuilder mailBuilder;
    private final MailService mailService;
    private static final String POST_URL = "";

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPost().getPostId())
                .creationDate(TimeAgo.using(comment.getCreationDate().toEpochMilli()))
                .userName(comment.getUser().getUsername())
                .build();
    }

    private Comment mapToComment(CommentRequest commentRequest, Post post) {
        User user = authService.getCurrentUser();
        return Comment.builder()
                .text(commentRequest.getText())
                .post(post)
                .creationDate(Instant.now())
                .user(user)
                .build();
    }

    public void save(CommentRequest commentRequest) {
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found with id: " + commentRequest.getPostId()));
        commentRepository.save(mapToComment(commentRequest, post));
        String message = mailBuilder.build(
                authService.getCurrentUser() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());


    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendEmail(new NotificationEmail(
                user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        return commentRepository.findByPost(post)
                .stream().map(this::mapToResponse).collect(toList());
    }


    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with: " + username));

        return commentRepository.findAllByUser(user)
                .stream().map(this::mapToResponse).collect(toList());
    }
}
