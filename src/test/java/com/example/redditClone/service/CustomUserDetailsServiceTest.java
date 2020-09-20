package com.example.redditClone.service;

import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CustomUserDetailsServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Test
    public void shouldReturnUserDetailsWhenLoadUserByUsernameIsCalledWithExistingUser() {
        User user = userRepository.save(new User(
                "Mutuba20",
                "mutuba20@gmail.com",
                "Thanks for joining subreddit"));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        assertEquals(userDetails.getUsername(), user.getUsername());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldRaiseUsernameNotFoundExceptionWhenLoadUserByUsernameIsCalledWithNonExistentUser() {
        User user = testUser();
        customUserDetailsService.loadUserByUsername(user.getUsername());
        throw new UsernameNotFoundException("Error sending activation email to "
                + user.getUsername());
    }


    @Test
    public void shouldReturnUserDetailsWhenLoadUserByIdIsCalledWithExistingUser() {
        User user = testUser();
        userRepository.save(user);
        UserDetails userDetails = customUserDetailsService.loadUserById(user.getUserId());
        assertEquals(userDetails.getUsername(), user.getUsername());

    }


    @Test(expected = UsernameNotFoundException.class)
    public void shouldRaiseUsernameNotFoundExceptionWhenLoadUserByIdIsCalledWithNonExistentUser() {
        customUserDetailsService.loadUserById(123L);
        throw new UsernameNotFoundException("Error sending activation email to "
                + 123);

    }


    // Helper function

    public User testUser() {
        return new User(
                "Mutuba2020",
                "mutuba2020@gmail.com",
                "Thanks for joining subreddit");

    }

}
