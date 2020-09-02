package com.example.redditClone.service;

import com.example.redditClone.dto.RegistrationRequest;
import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JavaMailSender sender;

    @Test
    public void shouldRegisterUserSuccessfully(){
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "Mutuba", "daniel@gmail.com", "Baraka1234");

        authService.register(registrationRequest);

        Optional<User> foundUser = userRepository.findByUsername(registrationRequest.getUsername());
        Assert.assertNotNull(foundUser.get());
        assertThat(foundUser.get().getEmail())
                .isEqualTo(registrationRequest.getEmail());
    }
}
