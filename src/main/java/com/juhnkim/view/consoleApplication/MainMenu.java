package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.service.PasswordService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.sql.SQLException;
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
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                            ** WELCOME! **                          ");
            System.out.println("                    What would you like to do today?                ");
            System.out.println("                        1. Register new account                     ");
            System.out.println("                        2. Login                                    ");
            System.out.println("                        0. Exit                                     ");
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
                userOption = -1; // Invalid option
            }
        } while (userOption != 0);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                    Exiting application...                          ");
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
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter name: ");
        System.out.println("--------------------------------------------------------------------");
        String name = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                  Enter your social security number:                ");
        System.out.println("--------------------------------------------------------------------");
        String ssn = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter email: ");
        System.out.println("--------------------------------------------------------------------");
        String email = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
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

        String hashedPassword = userService.validateAndHashPassword(password);

        // Sätt addUser som boolean istället eller skapa en seperat metod för att för att kolla om värden är rätt innan allt utförs
        User registeredUser = userService.addUser(new User(name, ssn, email, false, phone, address, hashedPassword));
        accountService.createBankAccount(registeredUser, "Default Account");

        System.out.println("--------------------------------------------------------------------");
        System.out.print(ConsoleColors.GREEN);
        System.out.println("                  User registered & account was opened!         ");
        System.out.print(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
    }
}

