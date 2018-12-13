package com.example.backendtest.authentication.exception;

public class UserAlreadyExistsException extends InvalidParameterException {
    public UserAlreadyExistsException() {
        super("user already exists");
    }
}
