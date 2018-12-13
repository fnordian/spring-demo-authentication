package com.example.backendtest.authentication.service;

import com.example.backendtest.authentication.entity.User;
import com.example.backendtest.authentication.exception.AuthenticationError;
import com.example.backendtest.authentication.exception.InvalidParameterException;
import com.example.backendtest.authentication.exception.UserAlreadyExistsException;
import com.example.backendtest.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserAuthenticationService {

    private UserRepository userRepository;
    private PasswordHasherService passwordHasherService;

    public UserAuthenticationService(UserRepository userRepository, PasswordHasherService passwordHasherService) {
        this.userRepository = userRepository;
        this.passwordHasherService = passwordHasherService;
    }

    /**
     * Checks if a user exists, and the correct password is provided
     * @param username the username
     * @param password the password
     * @return the user
     * @throws AuthenticationError if the user does not exist, or the password is wrong
     */
    public User authenticate(String username, String password) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .filter(user -> passwordHasherService.checkPassword(password, user.getPasswordHash()))
                .orElseThrow(AuthenticationError::new);
    }

    /**
     * Create a new user in the system
     * @param username name of the user
     * @param password password for logging in
     * @return the created user
     * @throws UserAlreadyExistsException when the user already exists
     */
    public User register(String username, String password) {
        if (StringUtils.isEmpty(password)) {
            throw new InvalidParameterException("password required");
        }

        if (userRepository.findByUsername(username) == null) {
            User user = new User(null, username, passwordHasherService.hashPassword(password));
            return userRepository.save(user);
        } else {
            throw new UserAlreadyExistsException();
        }
    }
}
