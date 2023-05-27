package com.juhnkim.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.juhnkim.model.User;

public class LoginService {
    private final UserService userService;
    private boolean isUserLogged = false;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public boolean isUserLogged() {
        return isUserLogged;
    }

    public void setUserLogged(boolean userLogged) {
        isUserLogged = userLogged;
    }

    public User handleLogin(String ssn, String password) {
        User user;
        try {
            user = userService.getUserBySsn(ssn);
        } catch (Exception e) {
            System.out.println("User with the provided ssn not found.");
            return null;
        }

        BCrypt.Result passwordMatch = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passwordMatch.verified) {
            setUserLogged(true);
            return user;
        } else {
            System.out.println("Incorrect password.");
            return null;
        }
    }
}
