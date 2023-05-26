package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.Account;
import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class AccountMenu {
    private final AccountService accountService;
    private final Scanner scan;


    public AccountMenu(AccountService accountService, Scanner scan) {
        this.accountService = accountService;
        this.scan = scan;
    }

    public void displayAccountMenu(User loggedInUser) {
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        1. Check balance                            ");
            System.out.println("                        2. Create new bank account                  ");
            System.out.println("                        3. Delete bank account                      ");
            System.out.println("                        6. Previous                                 ");
            System.out.println("--------------------------------------------------------------------");

            userOption = scan.nextInt();
            scan.nextLine();
            handleAccountMenu(userOption, loggedInUser);

        } while (userOption != 6);

    }

    public void handleAccountMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:
                handleAllUserAccounts(loggedInUser);
                break;
            case 2:
                handleCreateBankAccount(loggedInUser);
                break;
            case 3:
                handleDeleteBankAccount(loggedInUser);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    public void handleAllUserAccounts(User loggedInUser) {
        List<Account> userAccounts = accountService.getAllUserAccounts(loggedInUser);

        if (userAccounts.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        for (Account userAccount : userAccounts) {
            System.out.println(userAccount.getAccountName() + " " + userAccount.getAccountNumber() + " " + userAccount.getBalance());
        }

    }

    public void handleCreateBankAccount(User loggedInUser) {
        System.out.println("What would you like to call your account?");
        String accountName = scan.nextLine();
        boolean isAccountCreated = accountService.createBankAccount(loggedInUser, accountName);

        if (isAccountCreated) {
            System.out.println("Your new bank account has been created!");
        } else {
            System.out.println("Something went wrong..");
        }
    }

    public void handleDeleteBankAccount(User loggedInUser) {

        List<Account> allAccountsFromUser = accountService.getAccountById(loggedInUser.getId());

        if (allAccountsFromUser.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Here are your accounts:");
        int index = 1;
        for (Account account : allAccountsFromUser) {
            System.out.println(index + ". " + account.getAccountName() + " " + account.getAccountNumber());
            index++;
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Enter the number of the account you want to delete:");
        int accountIndex = scan.nextInt();
        Account accountToDelete = allAccountsFromUser.get(accountIndex - 1);

        System.out.println("--------------------------------------------------------------------");
        System.out.println(ConsoleColors.BLUE);
        System.out.println("           Your bank account will permanently be deleted.           ");
        System.out.println("        Are you still sure you want to close your account?          ");
        System.out.println("                                y/n:                                ");
        System.out.println(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
        String userOption = scan.nextLine();

        boolean isDeleted = false;
        if (userOption.equalsIgnoreCase("y")) {
            isDeleted = accountService.deleteBankAccount(accountToDelete);
        }

        if (isDeleted) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.BLUE);
            System.out.println("                Your account has been deleted!                      ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        } else {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("         Failed to delete the account. Please try again later.      ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        }
    }
}
