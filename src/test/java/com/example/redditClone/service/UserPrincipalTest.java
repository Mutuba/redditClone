package com.example.redditClone.service;

import com.example.redditClone.models.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPrincipalTest {

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Test
//    public void userPrincipalTesting(){
//        User user = new User();
//        user.setUsername("Mutush");
//        user.setPassword(passwordEncoder.encode("Baraka1234"));
//        user.setEmail("daniel@gmail.com");
//
//        UserPrincipal userPrincipal = UserPrincipal.create(user);
//
//    }
}
