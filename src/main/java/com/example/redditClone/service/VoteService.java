package com.example.redditClone.service;

import com.example.redditClone.dto.VoteDTO;
import com.example.redditClone.exception.PostNotFoundException;
import com.example.redditClone.exception.VoteException;
import com.example.redditClone.models.Post;
import com.example.redditClone.models.Vote;
import com.example.redditClone.repository.PostRepository;
import com.example.redditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.redditClone.models.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    private Vote mapToVote(VoteDTO voteDTO, Post post) {
        return Vote.builder()
                .voteType(voteDTO.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }

    @Transactional
    public void vote(VoteDTO voteDTO) {
        Post post = postRepository.findById(voteDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id:" + voteDTO.getPostId()));
        Optional<Vote> latestVoteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                post, authService.getCurrentUser());

        if (latestVoteForPostByUser.isPresent() && latestVoteForPostByUser.get()
                .getVoteType().equals(voteDTO.getVoteType())) {
            throw new VoteException("You've already " + voteDTO.getVoteType() + "'d this post");
        }
        if (UPVOTE.equals(voteDTO.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDTO, post));
        postRepository.save(post);
    }
}
