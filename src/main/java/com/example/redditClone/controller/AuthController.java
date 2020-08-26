package com.example.redditClone.controller;

import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
   private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity verify(@PathVariable String token) {
        authService.verifyToken(token);
        return new ResponseEntity<>("Account Activated", HttpStatus.OK);
    }
}
