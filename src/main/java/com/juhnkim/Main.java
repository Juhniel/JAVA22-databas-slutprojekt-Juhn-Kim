package com.juhnkim;

import com.juhnkim.database.DatabaseInitializer;
import com.juhnkim.repository.UserRepository;
import com.juhnkim.service.LoginService;
import com.juhnkim.service.PasswordService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleApplication.LoggedInMenu;
import com.juhnkim.view.consoleApplication.LoginMenu;
import com.juhnkim.view.consoleApplication.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        DatabaseInitializer c = new DatabaseInitializer();
//        c.createUserTable();
//        c.createAccountTable();
//        c.createTransactionTable();


        Scanner scan = new Scanner(System.in);
        UserRepository userRepository = new UserRepository();

        PasswordService passwordService = new PasswordService();

        UserService userService = new UserService(userRepository, passwordService);
        LoginService loginService = new LoginService(userService);

        LoggedInMenu loggedInMenu = new LoggedInMenu(loginService, scan);
        LoginMenu loginMenu = new LoginMenu(loginService, scan, loggedInMenu);
        MainMenu mainMenu = new MainMenu(userService, loginMenu, scan);

        mainMenu.displayMainMenu();
    }
}