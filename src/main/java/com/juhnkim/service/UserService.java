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

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserBySsn(String ssn) {
        return userRepository.getUserBySsn(ssn);
    }

    // Ändra statements
    public String hashPassword(String password) {
        return passwordService.hashPassword(password);
    }
}
