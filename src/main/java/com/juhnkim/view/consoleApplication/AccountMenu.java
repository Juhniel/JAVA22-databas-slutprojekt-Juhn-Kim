package com.juhnkim.view.consoleApplication;

import com.juhnkim.exception.DeleteDefaultAccountException;
import com.juhnkim.model.Account;
import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.List;
import java.util.Scanner;

/*
    This class handles all the user accounts and the user is also able to check the balance, open up a new bank account
    or deleting existing bank account.
*/
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
            System.out.print(ConsoleColors.WHITE);
            System.out.println("                            ** Account Menu **                      ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                        1. Check balance                            ");
            System.out.println("                        2. Create new bank account                  ");
            System.out.println("                        3. Delete bank account                      ");
            System.out.println("                        6. Previous                                 ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");

            try {
                userOption = Integer.parseInt(scan.nextLine());
                handleAccountMenu(userOption, loggedInUser);
            } catch (NumberFormatException e) {
                e.getMessage();
                userOption = -1;
            }
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

    /*
        Displaying all accounts and showing the name, number and balance of each account.
    */
    public void handleAllUserAccounts(User loggedInUser) {
        List<Account> userAccounts = accountService.getAllUserAccountsById(loggedInUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        for (Account userAccount : userAccounts) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("            Account name: " + userAccount.getAccountName());
            System.out.println("            Account number: " + userAccount.getAccountNumber());
            System.out.println("            Balance: " + userAccount.getBalance());
            System.out.println("--------------------------------------------------------------------\n");
        }
    }

    /*
        When user created a bank account we will also check in the service layer if the user already has a default
        account. If the user has a default account we will create a new regular account. If not we
        will set it to the users default account to be able to receive funds. All new accounts opened will have 1000
        in funds for test purposes.
    */
    public void handleCreateBankAccount(User loggedInUser) {
        System.out.println("What would you like to name your account?");
        String accountName = scan.nextLine();
        boolean isAccountCreated = accountService.createBankAccount(loggedInUser, accountName);

        if (isAccountCreated) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.GREEN);
            System.out.println("                Your account has been created!                      ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        } else {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("             Failed to create account. Please try again later.      ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        }
    }

    /*
        A note here is that the user can not delete his/hers default account if the user has other accounts
        available. This is because we want to make sure that the user receives funds to the account flagged as default.
    */
    public void handleDeleteBankAccount(User loggedInUser) {
        List<Account> allAccountsFromUser = accountService.getAllUserAccountsById(loggedInUser.getId());

        if (allAccountsFromUser.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                      You don't have any accounts.                  ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        Here are your accounts:                     ");
        int index = 1;
        for (Account account : allAccountsFromUser) {
            System.out.println(index + ". " + account.getAccountName() + " " + account.getAccountNumber());
            index++;
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Enter the number of the account you want to delete:");
        int accountIndex = scan.nextInt();
        scan.nextLine();
        Account accountToDelete = allAccountsFromUser.get(accountIndex - 1);

        System.out.println("--------------------------------------------------------------------");
        System.out.print(ConsoleColors.BLUE);
        System.out.println("           Your bank account will permanently be deleted.           ");
        System.out.println("        Are you still sure you want to close your account?          ");
        System.out.println("                                y/n:                                ");
        System.out.print(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
        String userOption = scan.nextLine();


        if (userOption.equalsIgnoreCase("y")) {
            try {
                accountService.deleteBankAccount(accountToDelete, allAccountsFromUser);
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.GREEN);
                System.out.println("                Your account has been deleted!                      ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
            } catch (DeleteDefaultAccountException e) {
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println(e.getMessage());
                System.out.print(ConsoleColors.RESET);
            }
        }
    }
}

