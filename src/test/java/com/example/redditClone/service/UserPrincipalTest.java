

package com.example.redditClone.service;

import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserPrincipalTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender sender;


    @Test
    public void shouldCreateUserPrincipal() throws Exception {

        User user = new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        );
        userRepository.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);


        Assert.assertNotNull(userPrincipal.getAuthorities());
        Assert.assertNotNull(userPrincipal.getUsername());
        Assert.assertEquals(user.getUsername(), userPrincipal.getUsername());
    }


    @Test
    public void testEqualsMethodContracts() {

        User user1 = new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        );
        userRepository.save(user1);

        User user2 = new User(
                "Mutush1",
                "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234")
        );
        userRepository.save(user2);

        UserPrincipal userPrincipal1 = UserPrincipal.create(user1);
        UserPrincipal userPrincipal2 = UserPrincipal.create(user2);
        Assert.assertNotEquals(userPrincipal1, userPrincipal2);

        Assert.assertFalse(userPrincipal1.equals(user1) || userPrincipal1.equals(null));
        Assert.assertTrue(userPrincipal1.equals(userPrincipal1));

    }

    @Test
    public void testHashCodeMethodContracts() {
        User user1 = new User(
                "Mutush",
                "daniel@gmail.com",
                passwordEncoder.encode("Baraka1234")
        );

        User user2 = new User(
                "Mutush1",
                "daniel1@gmail.com",
                passwordEncoder.encode("Baraka1234")
        );
        userRepository.save(user1);
        userRepository.save(user2);

        UserPrincipal userPrincipal1 = UserPrincipal.create(user1);
        UserPrincipal userPrincipal2 = UserPrincipal.create(user2);
        Assert.assertNotEquals(userPrincipal1.hashCode(), userPrincipal2.hashCode());
    }


}
