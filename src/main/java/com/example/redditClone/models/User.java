package com.example.redditClone.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;


@Data
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    @SequenceGenerator(name = "USER_GEN", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_GEN")
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;


    @Email
    @NotBlank(message = "Email is required")
    private String email;
    private Instant creationDate;
    private boolean accountStatus;

    public User( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
