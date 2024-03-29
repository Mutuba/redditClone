package com.example.redditClone.service;

import com.example.redditClone.dto.AuthenticationResponse;
import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.dto.RefreshTokenRequest;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.exception.ActivationException;
import com.example.redditClone.exception.UsernameNotFoundException;
import com.example.redditClone.models.*;
import com.example.redditClone.repository.RoleRepository;
import com.example.redditClone.repository.TokenRepository;
import com.example.redditClone.repository.UserRepository;
import com.example.redditClone.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.example.redditClone.config.Constants.EMAIL_ACTIVATION;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    private final RoleRepository roleRepository;

    private final MailBuilder mailBuilder;


    @Transactional
    public void register(RegistrationRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreationDate(Instant.now());
        user.setAccountStatus(false);
        userRepository.save(user);

        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);

        // Evaluate the optional if null create a user role
        Role role = userRole.orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_USER)));

        user.setRoles(Collections.singleton(role));

        String token = generateVerificationToken(user);
        String message = mailBuilder.build("Welcome to RedditMini. " +
                "Please visit the link below to activate you account : " + EMAIL_ACTIVATION + "/" + token);
        mailService.sendEmail(
                new NotificationEmail("Please Activate Your Account", user.getEmail(), message));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + principal.getUsername()));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = jwtTokenProvider.generateToken(authentication);
        return AuthenticationResponse
                .builder()
                .authenticationToken(authToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getJwtExpirationInMillis()))
                .username(authentication.getName())
                .build();
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        AccountVerificationToken verificationToken = new AccountVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        return token;
    }


    public void verifyToken(String token) {
        AccountVerificationToken accountVerificationToken = tokenRepository
                .findByToken(token).orElseThrow(() -> new ActivationException("Invalid Activation Token"));
        enableAccount(accountVerificationToken);
    }


    @Transactional
    public void enableAccount(AccountVerificationToken token) {
        User user = token.getUser();
        user.setAccountStatus(true);
        userRepository.save(user);

    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtTokenProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }


    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // authentication is there if it is not an instance of AnonymousAuthenticationToken
        // && isAuthenticated() returns true
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}