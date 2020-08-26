package com.example.redditClone.security;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import com.example.redditClone.exception.ActivationException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class JWTProvider {
    private KeyStore keystore;

    @PostConstruct
    public void init() {
        try {
            keystore = KeyStore.getInstance("JKS");
            InputStream resourceStream = getClass().getResourceAsStream("/keystore.jks");
            keystore.load(resourceStream, "password".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new ActivationException("Exception occured while loading keystore");
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivKey()).compact();
    }

    private PrivateKey getPrivKey () {
        try {
            return (PrivateKey) keystore.getKey("alias", "password".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new ActivationException("Exception occurred while retrieving public key");
        }
    }
}
