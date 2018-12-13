package com.example.backendtest.authentication.service;

import com.example.backendtest.authentication.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserTokenService {

    private String signingKey;

    public UserTokenService(@Value("${jwtSigningKey:}") String signingKey) {
        this.signingKey = signingKey;
    }

    public String issueForUser(User user, Instant now, long validSeconds) {
        return Jwts.builder()
                .claim("sub", user.getId())
                .claim("username", user.getUsername())
                .claim("iat", now.getEpochSecond())
                .claim("exp", now.plusSeconds(validSeconds).getEpochSecond())
                .signWith(SignatureAlgorithm.HS256, signingKey.getBytes())
                .compact();
    }

}
