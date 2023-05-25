package com.juhnkim.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordService {

    public String hashPassword(String password) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return hashedPassword;
    }
}
