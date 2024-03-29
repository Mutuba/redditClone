package com.example.redditClone.controller;

import com.example.redditClone.dto.APIResponse;
import com.example.redditClone.dto.SubredditDTO;
import com.example.redditClone.repository.SubredditRepository;
import com.example.redditClone.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    private final SubredditRepository subredditRepository;

    @GetMapping
    public List<SubredditDTO> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDTO getSubreddit(@PathVariable Long id) {
        return subredditService.getSubreddit(id);
    }

    @PostMapping
    public ResponseEntity addSubreddit(@RequestBody @Valid SubredditDTO subredditDTO) {
        if(subredditRepository.existsByName(subredditDTO.getName())){
            return new ResponseEntity(new APIResponse(false, "Subreddit name already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        SubredditDTO subredditDTO1 = subredditService.save(subredditDTO);
        return new ResponseEntity(subredditDTO1, HttpStatus.CREATED);
    }


}
