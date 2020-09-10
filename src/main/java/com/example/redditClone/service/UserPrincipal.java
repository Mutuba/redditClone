package com.example.redditClone.service;

import com.example.redditClone.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


public final class UserPrincipal implements UserDetails {
    private Long id;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }


    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                createAuthority("USER")

        );
    }

    private static Collection<? extends GrantedAuthority> createAuthority(String role) {
        // takes a role name and returns an instance of SimpleGrantedAuthority
        //Returns an immutable list containing only the specified object. The returned list is serializable.
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal)) {
            return false;
        }
        UserPrincipal that = (UserPrincipal) o;
        return getUsername().equals(that.getUsername()) &&
                getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail());
    }
}
