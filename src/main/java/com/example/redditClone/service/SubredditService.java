package com.example.redditClone.service;

import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.exception.SubredditNotFoundException;
import com.example.redditClone.models.Subreddit;
import com.example.redditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    // get a subreddit and map to what a user can see, example get one or many subreddits.
    private SubredditDTO mapToDTO (Subreddit subreddit) {
        return SubredditDTO.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .postCount(subreddit.getPosts().size())
                .build();

    }

    // maps to subreddit to be send to th db for creation of subreddit.
    private Subreddit mapToSubreddit (SubredditDTO subredditDTO) {
        return Subreddit.builder().name("/r/" + subredditDTO.getName())
                .description(subredditDTO.getDescription())
                .user(authService.getCurrentUser())
                .creationDate(Instant.now())
                .build();

    }

    @Transactional(readOnly = true)
    public List<SubredditDTO> getAll() {
        return StreamSupport
                .stream(subredditRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


//    @Test
//    public void whenConvertedToList_thenCorrect() {
//        Iterable<String> iterable
//                = Arrays.asList("Testing", "Iterable", "conversion", "to", "Stream");
//
//        List<String> result = StreamSupport.stream(iterable.spliterator(), false)
//                .map(String::toUpperCase)
//                .collect(Collectors.toList());
//
//        assertThat(
//                result, contains("TESTING", "ITERABLE", "CONVERSION", "TO", "STREAM"));
//    }


    @Transactional
    public SubredditDTO save(SubredditDTO subredditDTO) {
        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDTO));
        subredditDTO.setId(subreddit.getId());
        return subredditDTO;
    }

    @Transactional(readOnly = true)
    public SubredditDTO getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
        return mapToDTO(subreddit);
    }
}
