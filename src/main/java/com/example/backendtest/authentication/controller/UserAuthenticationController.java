package com.example.backendtest.authentication.controller;

import com.example.backendtest.authentication.controller.dto.AuthenticationRequest;
import com.example.backendtest.authentication.controller.dto.CreateUserRequest;
import com.example.backendtest.authentication.controller.dto.CreateUserResponse;
import com.example.backendtest.authentication.entity.User;
import com.example.backendtest.authentication.exception.AuthenticationError;
import com.example.backendtest.authentication.exception.InvalidParameterException;
import com.example.backendtest.authentication.service.UserAuthenticationService;
import com.example.backendtest.authentication.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Slf4j
public class UserAuthenticationController {

    private UserAuthenticationService userAuthenticationService;
    private UserTokenService userTokenService;
    private long tokenValidSeconds = 120;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService, UserTokenService userTokenService) {
        this.userAuthenticationService = userAuthenticationService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest authenticationRequest) {
        User user = userAuthenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return userTokenService.issueForUser(user, Instant.now(), tokenValidSeconds);
    }

    @PostMapping("/register")
    public CreateUserResponse registerNewUser(@RequestBody CreateUserRequest createUserRequest) {
        User newUser = userAuthenticationService.register(createUserRequest.getUsername(), createUserRequest.getPassword());
        return new CreateUserResponse(newUser.getId(), newUser.getUsername());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(InvalidParameterException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(AuthenticationError e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
