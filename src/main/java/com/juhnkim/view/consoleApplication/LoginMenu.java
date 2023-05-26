package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.LoginService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

public class LoginMenu {
    private final LoginService loginService;
    private User loggedInUser;
    private final Scanner scan;
    private final LoggedInMenu loggedInMenu;

    public LoginMenu(LoginService loginService, Scanner scan, LoggedInMenu loggedInMenu) {
        this.loginService = loginService;
        this.scan = scan;
        this.loggedInMenu = loggedInMenu;
    }

    public void displayLoginMenu() {

        int loginAttempts = 0;

        while (loggedInUser == null) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                                Login                               ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Enter your ssn: ");
            String ssn = scan.nextLine();
            System.out.println("Enter Password: ");
            String password = scan.nextLine();
            loggedInUser = loginService.handleLogin(ssn, password);

            if (loggedInUser != null) {
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.GREEN);
                System.out.println("                        Login successful!");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
                loggedInMenu.displayLoggedInMenu(loggedInUser);
            } else {
                loginAttempts++;
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                if (loginAttempts < 4) {
                    System.out.println("                    Login failed, try again!");
                    System.out.print(ConsoleColors.RESET);
                    System.out.println("--------------------------------------------------------------------");
                } else {
                    System.out.println("                       Try again later!");
                    System.out.print(ConsoleColors.RESET);
                    System.out.println("--------------------------------------------------------------------");
                    break;
                }
            }
        }
    }
}
