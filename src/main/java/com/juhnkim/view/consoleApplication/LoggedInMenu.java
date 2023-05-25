package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.LoginService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

public class LoggedInMenu {
    private final LoginService loginService;
    private final Scanner scan;

    public LoggedInMenu(LoginService loginService, Scanner scan) {
        this.loginService = loginService;
        this.scan = scan;
    }

    public void loggedInMenu(User loggedInUser) {
        if(loggedInUser == null) {
            return;
        }
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                Choose one of the menu options:");
            System.out.println("                        1. My account");
            System.out.println("                        2. Posts");
            System.out.println("                        0. Log out");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            userOption = Integer.parseInt(scan.nextLine());
            processLoggedInOption(userOption);
        } while (userOption != 0 && loginService.isUserLogged());
        System.out.println("Logging out...");
    }

    //  LOGGED IN MENU
    public void processLoggedInOption(int userOption) {
        switch (userOption) {
            case 1:
//                userMenu.displayUserMenu(loggedInUser);
                break;
            case 2:
//                postMenu.displayPostMenu(loggedInUser);
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
