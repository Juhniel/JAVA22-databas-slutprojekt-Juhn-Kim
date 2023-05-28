package com.juhnkim.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordService {

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
