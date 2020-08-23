package com.example.redditClone.repository;

import com.example.redditClone.models.AccountVerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<AccountVerificationToken, Long> {
    Optional<AccountVerificationToken> findByToken(String token);
}
