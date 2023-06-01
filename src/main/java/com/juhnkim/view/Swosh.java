package com.juhnkim.view;

import com.juhnkim.database.DatabaseInitializer;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;
import com.juhnkim.repository.UserRepository;
import com.juhnkim.service.*;
import com.juhnkim.view.consoleApplication.*;

import java.util.Scanner;

public class Swosh {

    public void initializeSQLTables() {
        DatabaseInitializer c = new DatabaseInitializer();
        c.createUserTable();
        c.createAccountTable();
        c.createTransactionTable();
    }

    public void runConsoleApplication(){
        Scanner scan = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        UserRepository userRepository = new UserRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        PasswordService passwordService = new PasswordService();
        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService(transactionRepository);
        UserService userService = new UserService(userRepository, passwordService);
        LoginService loginService = new LoginService(userService);

        TransactionMenu transactionMenu = new TransactionMenu(userService, accountService, transactionService, scan);
        AccountMenu accountMenu = new AccountMenu(accountService, scan);
        UserMenu userMenu = new UserMenu(userService, loginService, scan);
        LoggedInMenu loggedInMenu = new LoggedInMenu(transactionMenu, accountMenu, userMenu, loginService, scan);
        LoginMenu loginMenu = new LoginMenu(loginService, scan, loggedInMenu);
        MainMenu mainMenu = new MainMenu(userService, accountService, loginMenu, scan);

        mainMenu.displayMainMenu();
    }
}
