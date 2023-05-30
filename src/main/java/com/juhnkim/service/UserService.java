package com.juhnkim.service;

import com.juhnkim.exception.SameUserTransferException;
import com.juhnkim.exception.UserNotFoundException;
import com.juhnkim.model.User;
import com.juhnkim.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;


    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }


    private String validateEmail(User user) {
        String email = user.getEmail().trim();
        if(!email.contains("@") && !email.contains(".")){
            throw new IllegalArgumentException("Invalid email");
        }
        return email;
    }

    private String validateSSN(User user) {
        String ssn = user.getSsn().trim();
        if (ssn.length() < 10 || ssn.length() > 13) {
            throw new IllegalArgumentException("Invalid Social security number");
        }

        if(ssn.contains("-")){
            ssn.replace("-", "");
        }

      return ssn;
    }

    public User addUser(User user) {


        String phone = user.getPhone().trim();
        String address = user.getAddress().trim();

        // If all validation checks pass, update the user object and add it to the database
        user.setSsn(validateSSN(user));
        user.setEmail(validateEmail(user));
        user.setPhone(phone);
        user.setAddress(address);

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

    public User getUserByPhone(String phone, User loggedInUser) {
        User user = userRepository.getUserByPhone(phone);

        if (user == null) {
            throw new UserNotFoundException(phone);
        }

        if (phone.equals(loggedInUser.getPhone())) {
            throw new SameUserTransferException();
        }
        return user;
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
        if (password == null || password.trim().length() <= 5) {
            return false;
        }

        // check if password contains at least one special character
        if (!password.matches(".*\\p{Punct}.*")) {
            return false;
        }

        // check if password contains any whitespace
        if (password.contains(" ")) {
            return false;
        }
        return true;
    }
}
