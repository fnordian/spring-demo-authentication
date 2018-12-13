package com.example.backendtest.authentication.service;

import com.example.backendtest.authentication.entity.User;
import com.example.backendtest.authentication.exception.UserAlreadyExistsException;
import com.example.backendtest.authentication.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAuthenticationServiceTest {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void registerNewUserSucceeds() {
        User newUser = userAuthenticationService.register("foo", "bar");
        assertNotNull(newUser);
    }

    @Test
    public void registerExistingUserFails() {
        boolean exceptionCaught = false;

        try {
            User newUser = userAuthenticationService.register("foo", "bar");
            User newUser2 = userAuthenticationService.register("foo", "bar");
        } catch (UserAlreadyExistsException e) {
            exceptionCaught = true;
        }

        assertTrue(exceptionCaught);
    }
}