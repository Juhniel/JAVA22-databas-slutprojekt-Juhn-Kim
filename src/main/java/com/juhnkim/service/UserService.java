package com.juhnkim.service;

import com.juhnkim.model.User;
import com.juhnkim.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;


    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public boolean addUser(User user) {
        return userRepository.addUser(user);
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(User user) {
        return userRepository.deleteUser(user);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserBySsn(String ssn) {
        return userRepository.getUserBySsn(ssn);
    }


    public String validateAndHashPassword(String password) {
        if (validatePassword(password)) {
            return passwordService.hashPassword(password);
        } else {
            // you can throw an exception here or return a special value to indicate that the password was invalid
            throw new IllegalArgumentException("Invalid password");
        }
    }

    private boolean validatePassword(String password) {
        // Validate the password according to your rules. Here is a simple example
        return password != null && password.length() >= 4;
    }
}
