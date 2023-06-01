package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.LoginService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

/*
    This class handles displaying the login menu for the user and routing the user to the Swosh menu if the login was
    successful.
*/
public class LoginMenu {
    private final LoginService loginService;
    private final Scanner scan;
    private final LoggedInMenu loggedInMenu;

    public LoginMenu(LoginService loginService, Scanner scan, LoggedInMenu loggedInMenu) {
        this.loginService = loginService;
        this.scan = scan;
        this.loggedInMenu = loggedInMenu;
    }

    public void displayLoginMenu() {

        int loginAttempts = 0;

        while (!loginService.isUserLogged()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.WHITE);
            System.out.println("                              ** LOGIN **                           ");
            System.out.println("                       Enter 'exit' to go back to menu              ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Enter your ssn: ");
            String ssn = scan.nextLine();
            if(ssn.equalsIgnoreCase("exit")){
                break;
            }
            System.out.println("Enter Password: ");
            String password = scan.nextLine();
            if(password.equalsIgnoreCase("exit")){
                break;
            }
            User loggedInUser = loginService.handleLogin(ssn, password);

            loginAttempts = handleLoginResult(loggedInUser, loginAttempts);

            if (loginAttempts == -1) {
                break;
            }
        }
    }

    private int handleLoginResult(User loggedInUser, int loginAttempts) {
        if (loggedInUser != null) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.GREEN);
            System.out.println("                        Login successful!");
            System.out.println("                        Welcome " + loggedInUser.getName() +"!      ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            loggedInMenu.displayLoggedInMenu(loggedInUser);
            return -1;
        } else {
            loginAttempts++;
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            if (loginAttempts < 4) {
                System.out.println("                    Login failed, try again!                    ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
            } else {
                System.out.println("                       Try again later!");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
                return -1;
            }
        }
        return loginAttempts;
    }
}
