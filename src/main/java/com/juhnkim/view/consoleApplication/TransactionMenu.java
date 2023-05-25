package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;

import java.util.Scanner;

public class TransactionMenu {

    private final Scanner scan;



    public void displayTransactionMenu(User loggedInUser) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        1. Add balance to account                   ");
        System.out.println("                        2. Transfer funds                           ");
        System.out.println("                        3. Show all transactions                    ");
        System.out.println("                        4. Show transactions by date                ");
        System.out.println("                        6. Previous                                 ");
        System.out.println("--------------------------------------------------------------------");

        int userOption = scan.nextInt();
        scan.nextLine();
        handleTransactionMenu(userOption, loggedInUser);
    }

    public void handleTransactionMenu(int userOption, User loggedInUser) {
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
