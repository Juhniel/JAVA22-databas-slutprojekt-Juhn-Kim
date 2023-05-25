package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;

import java.util.Scanner;

public class AccountMenu {

    private final Scanner scan;


    public void displayAccountMenu(User loggedInUser) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        1. Check balance                            ");
        System.out.println("                        2. Create new bank account                  ");
        System.out.println("                        3. Delete bank account                      ");
        System.out.println("                        6. Previous                                 ");
        System.out.println("--------------------------------------------------------------------");

        int userOption = scan.nextInt();
        scan.nextLine();
        handleAccountMenu(userOption, loggedInUser);
    }

    public void handleAccountMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 6:
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}
