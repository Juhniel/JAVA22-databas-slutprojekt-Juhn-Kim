package com.juhnkim.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String phone) {
        super("User with phone number: " + phone + " not found.");
    }
}
