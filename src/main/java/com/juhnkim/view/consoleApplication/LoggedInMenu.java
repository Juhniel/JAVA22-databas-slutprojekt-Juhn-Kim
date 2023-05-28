package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.Account;
import com.juhnkim.model.User;
import com.juhnkim.service.LoginService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.Scanner;

public class LoggedInMenu {

    private final TransactionMenu transactionMenu;
    private final AccountMenu accountMenu;
    private final UserMenu userMenu;
    private final LoginService loginService;
    private final Scanner scan;

    public LoggedInMenu(TransactionMenu transactionMenu, AccountMenu accountMenu, UserMenu userMenu, LoginService loginService, Scanner scan) {
        this.transactionMenu = transactionMenu;
        this.accountMenu = accountMenu;
        this.userMenu = userMenu;
        this.loginService = loginService;
        this.scan = scan;
    }

    public void displayLoggedInMenu(User loggedInUser) {
        if(loggedInUser == null) {
            return;
        }
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                Choose one of the menu options:                     ");
            System.out.println("                        1. Transactions                             ");
            System.out.println("                        2. Bank account                             ");
            System.out.println("                        3. User account                             ");
            System.out.println("                        0. Log out                                  ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            userOption = Integer.parseInt(scan.nextLine());
            handleLoggedInMenu(userOption, loggedInUser);
        } while (userOption != 0 && loginService.isUserLogged());
        System.out.println("                        Logging out..                             ");
    }


    public void handleLoggedInMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:
                transactionMenu.displayTransactionMenu(loggedInUser);
                break;
            case 2:
                accountMenu.displayAccountMenu(loggedInUser);
                break;
            case 3:
                userMenu.displayUserMenu(loggedInUser);
            case 0:
                loginService.setUserLogged(false);
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
