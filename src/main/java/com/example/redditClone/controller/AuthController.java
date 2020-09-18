package com.example.redditClone.controller;

import com.example.redditClone.dto.APIResponse;
import com.example.redditClone.dto.AuthenticationResponse;
import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
   private AuthService authService;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity register( @Valid @RequestBody RegistrationRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity(new APIResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity(new APIResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity verify(@PathVariable String token) {
        authService.verifyToken(token);
        return new ResponseEntity<>("Account Activated", HttpStatus.OK);
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {

        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        return new ResponseEntity(authenticationResponse, HttpStatus.OK);
    }
}
