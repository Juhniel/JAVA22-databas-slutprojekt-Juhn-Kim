package com.juhnkim.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordService {

    public static String hashPassword(String password) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        System.out.println(hashedPassword);
        return hashedPassword;
    }


}
