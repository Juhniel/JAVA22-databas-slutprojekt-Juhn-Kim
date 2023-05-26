package com.juhnkim;

import com.juhnkim.database.DatabaseInitializer;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;
import com.juhnkim.repository.UserRepository;
import com.juhnkim.service.*;
import com.juhnkim.view.consoleApplication.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        DatabaseInitializer c = new DatabaseInitializer();
//        c.createUserTable();
//        c.createAccountTable();
//        c.createTransactionTable();


        Scanner scan = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        UserRepository userRepository = new UserRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        TransactionService transactionService = new TransactionService(transactionRepository);
        PasswordService passwordService = new PasswordService();
        AccountService accountService = new AccountService(accountRepository);
        UserService userService = new UserService(userRepository, passwordService);
        LoginService loginService = new LoginService(userService);

        TransactionMenu transactionMenu = new TransactionMenu(accountService, transactionService, scan);
        AccountMenu accountMenu = new AccountMenu(accountService, scan);
        UserMenu userMenu = new UserMenu(userService, loginService, scan);
        LoggedInMenu loggedInMenu = new LoggedInMenu(transactionMenu, accountMenu, userMenu, loginService, scan);
        LoginMenu loginMenu = new LoginMenu(loginService, scan, loggedInMenu);
        MainMenu mainMenu = new MainMenu(userService, loginMenu, scan);

        mainMenu.displayMainMenu();
    }
}