package com.example.redditClone.security;

import com.example.redditClone.service.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

// provides a user token based on a user who is trying to login
// the class also validates a user token
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // get value from properties file
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    // param authentication passed from AuthController has principal (user object), authorities, boolean authenticated true
    // getPrincipal is getter that returns the principal object
    public String generateToken(Authentication authentication) {

        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (!(authentication instanceof AnonymousAuthenticationToken)) {
        //     String currentUserName = authentication.getName();
        //     return currentUserName;
        //  }
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // System.out.println("User has authorities: " + userDetails.getAuthorities());

        // casting spring security principal to the custom user details object (UserPrincipal)
        // in the controller a user can be found as:
        // Principal principal = request.getUserPrincipal();
        // then principal.getName()
        // think of django request.user

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    //retrieve username from jwt token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.", ex.getMessage());
        }
        return false;
    }
}
