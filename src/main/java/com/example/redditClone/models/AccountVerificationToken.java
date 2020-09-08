package com.example.redditClone.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "token")
@Entity
public class AccountVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant expirationDate;

    public AccountVerificationToken(String token, User user, Instant expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }
}

