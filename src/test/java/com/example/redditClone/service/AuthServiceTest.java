package com.example.redditClone.service;

import com.example.redditClone.dto.AuthenticationResponse;
import com.example.redditClone.dto.LoginRequest;
import com.example.redditClone.dto.RefreshTokenRequest;
import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.exception.UsernameNotFoundException;
import com.example.redditClone.models.AccountVerificationToken;
import com.example.redditClone.models.RefreshToken;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.RefreshTokenRepository;
import com.example.redditClone.repository.TokenRepository;
import com.example.redditClone.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender sender;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Mutuba", "daniel@gmail.com", "Baraka1234");

        authService.register(registrationRequest);

        Optional<User> foundUser = userRepository.findByUsername(registrationRequest.getUsername());
        Assert.assertNotNull(foundUser.get());
        assertThat(foundUser.get().getEmail())
                .isEqualTo(registrationRequest.getEmail());
    }


    @Test
    public void shouldGetCurrentUserPrincipal() throws Exception {

        User user = new User();
        user.setUsername("Mutush1");
        user.setPassword(passwordEncoder.encode("Baraka1234"));
        user.setEmail("daniel1@gmail.com");

        userRepository.save(user);

        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        UserPrincipal userPrincipal = new UserPrincipal(123L,
                "Mutush1", "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userPrincipal);

        User currentUser = authService.getCurrentUser();

        Assert.assertNotNull(currentUser);
        assertThat(currentUser.getEmail())
                .isEqualTo(userPrincipal.getEmail());
    }


    @Test(expected = UsernameNotFoundException.class)
    public void testUsernameNotFoundException() throws Exception {

        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        UserPrincipal userPrincipal = new UserPrincipal(123L,
                "Mutush1", "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userPrincipal);
        authService.getCurrentUser();

        throw new UsernameNotFoundException("User not found with username: " + userPrincipal.getUsername());
    }


    @Test
    public void testLoginSuccessfulForCorrectUsername_Password() throws Exception {

        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        UserPrincipal userPrincipal = new UserPrincipal(123L,
                "Mutush1", "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);
        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userPrincipal);

        LoginRequest loginRequest = new LoginRequest("Mutush1", "Baraka1234");

        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        Assert.assertNotNull(authenticationResponse.getAuthenticationToken());
        assertThat(authenticationResponse.getUsername())
                .isEqualTo(userPrincipal.getUsername());


    }

    @Test
    public void testVerifyTokenSuccess() throws Exception {

        User user = new User();
        user.setUsername("Mutush1");
        user.setPassword(passwordEncoder.encode("Baraka1234"));
        user.setEmail("daniel1@gmail.com");
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        AccountVerificationToken accountVerificationToken = tokenRepository.save(AccountVerificationToken
                .builder()
                .token(token)
                .user(user)
                .build());

        authService.verifyToken(token);

        Assert.assertNotNull(accountVerificationToken.getToken());

    }

    @Test
    public void testRefreshToken() throws Exception {
        Collection<GrantedAuthority> grantedAuthority = Arrays.asList(
                new SimpleGrantedAuthority("USER")
        );

        UserPrincipal userPrincipal = new UserPrincipal(123L,
                "Mutush1", "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234"), grantedAuthority);
        Mockito.when(customUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userPrincipal);

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setCreatedDate(Instant.now());
        refreshTokenRepository.save(refreshToken);

        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest
                .builder().refreshToken(token).username("Mutush1").build();


        AuthenticationResponse authenticationResponse = authService.refreshToken(refreshTokenRequest);
        Assert.assertNotNull(authenticationResponse.getAuthenticationToken());
        assertThat(authenticationResponse.getUsername())
                .isEqualTo(userPrincipal.getUsername());


    }


}