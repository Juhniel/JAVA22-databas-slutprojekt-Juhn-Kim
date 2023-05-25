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

    String ssn;
    String name;
    String email;
    String phone;
    String address;
    String password;


    public void displayMainMenu() {
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                            ** WELCOME! **                          ");
            System.out.println("                    What would you like to do today?                ");
            System.out.println("                        1. Create new account                       ");
            System.out.println("                        2. Login                                    ");
            System.out.println("                        0. Exit                                     ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            userOption = scan.nextInt();
            scan.nextLine();
            processMainMenuOption(userOption);
        } while (userOption != 0);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                    Exiting application...                          ");
        System.out.println("--------------------------------------------------------------------");
    }

    public void processMainMenuOption(int userOption) {
        switch (userOption) {
            case 1:
                createNewUser();
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


    public void createNewUser() {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter name: ");
        System.out.println("--------------------------------------------------------------------");
        name = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter your social security number: ");
        System.out.println("--------------------------------------------------------------------");
        ssn = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter email: ");
        System.out.println("--------------------------------------------------------------------");
        email = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter password: ");
        System.out.println("--------------------------------------------------------------------");
        password = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter phone: ");
        System.out.println("--------------------------------------------------------------------");
        phone = scan.nextLine();
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Enter address: ");
        System.out.println("--------------------------------------------------------------------");
        address = scan.nextLine();

        String hashedPassword = userService.hashPassword(password);

        userService.addUser(new User(name, ssn, email, false, phone, address, hashedPassword));

    }
}

