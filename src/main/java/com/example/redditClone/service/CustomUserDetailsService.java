package com.example.redditClone.service;

import com.example.redditClone.models.User;
import com.example.redditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class UserInformationServiceImpl implements UserDetailsService {
//    @Autowired
//    UserRepository userRepository;
//
//    private Collection<? extends GrantedAuthority> fetchAuthorities (String role) {
//        // takes a role name and returns an instance of SimpleGrantedAuthority
//        //Returns an immutable list containing only the specified object. The returned list is serializable.
//        return Collections.singletonList(new SimpleGrantedAuthority(role));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> optionalUser = userRepository.findByUsername(username);
//        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(),
//                user.isAccountStatus(),
//                true,
//                true,
//                true,
//                fetchAuthorities("USER"));
//    }
//}



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email

        // looks for a user with the username or email
        // when successful it returns a user principal object
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + username)
                );

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        // looks for a user with the id
        // when successful it returns a user principal object
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}
