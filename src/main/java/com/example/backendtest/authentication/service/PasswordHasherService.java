package com.example.backendtest.authentication.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHasherService {
    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return bcrypt.encode(password); // use default strength
    }

    public boolean checkPassword(String password, String hash) {
        return bcrypt.matches(password, hash);
    }
}
