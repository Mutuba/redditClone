package com.example.redditClone.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
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

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Validator validator;

    private User user;
    private User user1;



    @Before
    public void setUp() {
        user = new User("Mutuba", "daniel@gmail.com", "TestPassword");
        user1 = new User("Mutuba1", "daniel1@gmail.com", "TestPassword");

    }

    @Test
    public void saveUser() {
        User savedUser = this.entityManager.persistAndFlush(user);
        assertThat(savedUser.getEmail()).isEqualTo("daniel@gmail.com");
        assertThat(savedUser.getPassword()).isEqualTo("TestPassword");

    }


    @Test(expected = ConstraintViolationException.class)
    public void whenBlankUserName_and_Email_thenOneConstraintViolation() {
        User user = new User();
        User saved = this.entityManager.persistAndFlush(user);
    }


    @Test
    public void when_ID_AccountStatus_and_CreationDate_Are_Set() {
        user.setCreationDate(Instant.now());
        user.setAccountStatus(true);
        User savedUser = this.entityManager.persistAndFlush(user);

        Assert.assertNotNull(savedUser.getCreationDate());
        Assert.assertNotNull(savedUser.getUserId());
        Assert.assertTrue(savedUser.isAccountStatus());
    }


    @Test
    public void when_ToString_Method_Is_Called() {
        User savedUser = this.entityManager.persistAndFlush(user);

        String userString = savedUser.toString();

        Assert.assertNotNull(userString);
    }


    @Test
    public void equalsHashCodeContracts() {
        EqualsVerifier.forClass(User.class).verify();
    }

    @Test
    public void whenEqualsMethod() {
        User savedUser = this.entityManager.persistAndFlush(user);

        User savedUser1 = this.entityManager.persistAndFlush(user1);
        assertThat(savedUser.equals(savedUser1)).isEqualTo(false);
        assertThat(savedUser.getUserId()).isNotEqualTo(savedUser1.getUserId());
    }


    @Test
    public void whenEqualsMethodReturnsTrue() {
        User savedUser = this.entityManager.persistAndFlush(user);
        User savedUser1 = this.entityManager.persistAndFlush(user);

        assertThat(savedUser.equals(savedUser1)).isEqualTo(true);

    }
    @Test
    public void neverEqualsNull() {
        User savedUser = this.entityManager.persistAndFlush(user);
        Assert.assertNotEquals(savedUser, null);
    }

}
