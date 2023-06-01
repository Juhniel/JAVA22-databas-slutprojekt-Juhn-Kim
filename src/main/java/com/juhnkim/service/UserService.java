package com.juhnkim.service;

import com.juhnkim.exception.SameUserTransferException;
import com.juhnkim.exception.UserNotFoundException;
import com.juhnkim.model.User;
import com.juhnkim.repository.UserRepository;

/*
    This class validates the users inputs before passing them to the repository classes where we handle methods to
    the mySQL database. Validating user inputs and catching error.
*/
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;


    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public String validateReceiverPhone(String phone) {
        phone = phone.trim();

        if(phone.contains("-")){
            phone = phone.replace("-", "");
        }

        if(userRepository.getUserByPhone(phone) == null) {
            throw new UserNotFoundException(phone);
        }
        return phone;
    }

    public String validatePhone(String phone) {
        phone = phone.trim();

        if(phone.length() < 10 || phone.length() > 11) {
            throw new IllegalArgumentException("Not a valid Swedish cellphone number");
        }

        if(phone.contains("-")){
            phone = phone.replace("-", "");
        }

        if(userRepository.getUserByPhone(phone) != null) {
            throw new IllegalArgumentException("User with that phone number already exists");
        }
        return phone;
    }

    private String validateEmail(String email) {
        email = email.trim();
        if(!email.contains("@") && !email.contains(".")){
            throw new IllegalArgumentException("Invalid email");
        }
        return email;
    }

    private String validateSSN(String ssn) {
        ssn = ssn.trim();

        if(userRepository.getUserBySsn(ssn) != null) {
            throw new IllegalArgumentException("User already exist with the same SSN");
        }

        if (ssn.length() < 12 || ssn.length() > 13) {
            throw new IllegalArgumentException("Invalid Social security number - Enter Format: 19891120-5569");
        }

        if(ssn.contains("-")){
            ssn = ssn.replace("-", "");
        }
      return ssn;
    }

    public User addUser(User user) {
        String address = user.getAddress().trim();

        user.setSsn(validateSSN(user.getSsn()));
        user.setEmail(validateEmail(user.getEmail()));
        user.setPhone(validatePhone(user.getPhone()));
        user.setAddress(address);

        return userRepository.addUser(user);
    }

    public void updateUser(User user) {

        user.setPassword(validateAndHashPassword(user.getPassword()));
        user.setEmail(validateEmail(user.getEmail()));
        user.setPhone(validatePhone(user.getPhone()));

        userRepository.updateUser(user);
    }

    public boolean deleteUser(User user) {
        return userRepository.deleteUser(user);
    }

    // Might be used in the future do not remove :)
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
            throw new IllegalArgumentException("Invalid password format/length.");
        }
    }

    private boolean validatePassword(String password) {
        if (password == null || password.trim().length() <= 5) {
            return false;
        }

        if (!password.matches(".*\\p{Punct}.*")) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (password.contains(" ")) {
            return false;
        }
        return true;
    }
}
