package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.PasswordService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

public class MainMenu {

    private final UserService userService;
    private final LoginMenu loginMenu;
    private final Scanner scan;

    public MainMenu(UserService userService, LoginMenu loginMenu, Scanner scan) {
        this.userService = userService;
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
            userOption = scan.nextInt();
            scan.nextLine();
            handleMainMenu(userOption);
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

        boolean isUserAdded = userService.addUser(new User(name, ssn, email, false, phone, address, hashedPassword));
        if (isUserAdded) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.GREEN);
            System.out.println("                        User added!                                 ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        } else {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                        Failed to add user - Try again later        ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        }
    }
}

