package com.juhnkim.view.consoleApplication;

import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

public class MainMenu {

    private final UserService userService;

    private final Scanner scan;

    public MainMenu(UserService userService, Scanner scan) {
        this.userService = userService;
        this.scan = scan;
    }


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
                // call
                break;
            case 2:
//                System.out.println("2");
                break;
            case 0:
                break;
            default:
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println("                Invalid option. Please try again.");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
        }
    }
}

