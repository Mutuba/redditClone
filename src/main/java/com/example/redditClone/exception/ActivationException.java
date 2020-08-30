package com.example.redditClone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ActivationException extends RuntimeException {
    public ActivationException(String message) {
        super(message);
    }
}
