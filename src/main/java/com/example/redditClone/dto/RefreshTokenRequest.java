package com.example.redditClone.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
    private String username;
}
