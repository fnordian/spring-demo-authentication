package com.example.backendtest.authentication.service;

import com.example.backendtest.authentication.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class UserTokenServiceTest {

    @Test
    public void issueForUser() {
        User user = new User(1L, "foo", "bar");
        Instant now = Instant.EPOCH;
        String secretKey = "secret key";
        String token = new UserTokenService(secretKey).issueForUser(user, now, 60);

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .setClock(() -> Date.from(now))
                .parseClaimsJws(token);

        assertEquals(String.valueOf(user.getId()), claimsJws.getBody().getSubject());
    }
}