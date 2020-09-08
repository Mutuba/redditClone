

package com.example.redditClone.models;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AccountVerificationTokenTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private AccountVerificationToken accountVerificationToken;

    @Before
    public void setUp() {
        accountVerificationToken = new AccountVerificationToken();
        user = new User("Mutuba", "daniel@gmail.com", "TestPassword");
    }


    @Test
    public void createAccountVerificationToken() {
        User savedUser = this.entityManager.persistAndFlush(user);
        accountVerificationToken.setUser(savedUser);
        accountVerificationToken.setExpirationDate(Instant.now());
        accountVerificationToken.setId(123L);
        accountVerificationToken.setToken("simpletoken");
        AccountVerificationToken savedVerificationToken = this.entityManager.merge(accountVerificationToken);
        assertThat(savedVerificationToken.getId()).isNotNull();
        assertThat(savedVerificationToken.getUser()).isEqualTo(savedUser);
        assertThat(savedVerificationToken.getExpirationDate()).isNotNull();

    }

    @Test
    public void createAccountVerificationTokenWithConstructor() {
        User savedUser = this.entityManager.persistAndFlush(user);
        AccountVerificationToken accountVerificationToken =  new AccountVerificationToken(123L,
                "token", savedUser, Instant.now());
        AccountVerificationToken savedVerificationToken = this.entityManager.merge(accountVerificationToken);
        assertThat(savedVerificationToken.getId()).isNotNull();
        assertThat(savedVerificationToken.getUser()).isEqualTo(savedUser);
        assertThat(savedVerificationToken.getExpirationDate()).isNotNull();

    }


}
