package com.github.gabrielsmartins.taskmanager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.gabrielsmartins.taskmanager.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user, Integer expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

            return JWT.create()
                    .withIssuer("task-manager")
                    .withSubject(user.getUsername())
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(expiration)))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

            return JWT.require(algorithm)
                    .withIssuer("task-manager")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}
