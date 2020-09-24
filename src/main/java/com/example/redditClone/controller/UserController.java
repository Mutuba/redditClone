package com.example.redditClone.controller;

import com.example.redditClone.dto.UserSummary;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.security.CurrentUser;
import com.example.redditClone.service.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(),
                currentUser.getUsername(), currentUser.getEmail());
        return userSummary;
    }


}