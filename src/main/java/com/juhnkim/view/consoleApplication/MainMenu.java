package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.service.PasswordService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class MainMenu {

    private final UserService userService;
    private final AccountService accountService;
    private final LoginMenu loginMenu;
    private final Scanner scan;

    public MainMenu(UserService userService, AccountService accountService, LoginMenu loginMenu, Scanner scan) {
        this.userService = userService;
        this.accountService = accountService;
        this.loginMenu = loginMenu;
        this.scan = scan;
    }

    public void displayMainMenu() {
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.WHITE);
            System.out.println("                            ** Main Menu **                         ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                            ** WELCOME! **                          ");
            System.out.println("                          1. Register new account                   ");
            System.out.println("                          2. Login                                  ");
            System.out.println("                          0. Exit                                   ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            String userInput = scan.nextLine();
            try {
                userOption = Integer.parseInt(userInput);
                handleMainMenu(userOption);
            } catch (NumberFormatException e) {
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println("                Invalid option. Please enter a number.              ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
                userOption = -1;
            }
        } while (userOption != 0);
        System.out.println("--------------------------------------------------------------------");
        System.out.print(ConsoleColors.BLUE);
        System.out.println("                    Exiting application...                          ");
        System.out.print(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
    }

    public void handleMainMenu(int userOption) {
        switch (userOption) {
            case 1:
                createNewUserAccount();
                break;
            case 2:
                loginMenu.displayLoginMenu();
                break;
            case 0:
                break;
            default:
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println("                Invalid option. Please try again.                   ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
        }
    }


    public void createNewUserAccount() {
        while (true) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        Enter name: ");
            System.out.println("--------------------------------------------------------------------");
            String name = scan.nextLine();
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        (YYYYMMDD-XXXX)                             ");
            System.out.println("                  Enter your social security number:                ");
            System.out.println("--------------------------------------------------------------------");
            String ssn = scan.nextLine();
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        Enter email: ");
            System.out.println("--------------------------------------------------------------------");
            String email = scan.nextLine();
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Make sure your password contains 1 special letter & 1 uppercase letter");
            System.out.println("                        Enter password: ");
            System.out.println("--------------------------------------------------------------------");
            String password = scan.nextLine();
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        Enter phone: ");
            System.out.println("--------------------------------------------------------------------");
            String phone = scan.nextLine();
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        Enter address: ");
            System.out.println("--------------------------------------------------------------------");
            String address = scan.nextLine();

            try {
                String hashedPassword = userService.validateAndHashPassword(password);
                User registeredUser = userService.addUser(new User(ssn, name, email, false, phone, address, hashedPassword));
                accountService.createBankAccount(registeredUser, "Default Account");

                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.GREEN);
                System.out.println("                  User registered & account was opened!         ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println("Error: " + e.getMessage());
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
            }
        }
    }
}

