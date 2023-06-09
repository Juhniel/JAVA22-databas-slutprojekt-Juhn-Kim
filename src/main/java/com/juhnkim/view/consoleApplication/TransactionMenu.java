package com.juhnkim.view.consoleApplication;

import com.juhnkim.exception.InsufficientFundsException;
import com.juhnkim.exception.SameUserTransferException;
import com.juhnkim.exception.UserNotFoundException;
import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.service.TransactionService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/*
 This class provides the Transaction menu for the console application.
 It leverages UserService, AccountService, and TransactionService to handle transaction operations for a User.
 The console menu supports operations such as transferring funds between users, viewing all transactions,
 and filtering transactions by date.
*/

public class TransactionMenu {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scan;

    public TransactionMenu(UserService userService, AccountService accountService, TransactionService transactionService, Scanner scan) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.scan = scan;
    }

    public void displayTransactionMenu(User loggedInUser) {
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.WHITE);
            System.out.println("                         ** Transaction Menu **");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.BLUE);
            System.out.println("                        1. Transfer funds                           ");
            System.out.println("                        2. Show all transactions                    ");
            System.out.println("                        3. Show transactions by date                ");
            System.out.println("                        6. Previous                                 ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");


            try {
                userOption = Integer.parseInt(scan.nextLine());
                handleTransactionMenu(userOption, loggedInUser);
            } catch (NumberFormatException e) {
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RED);
                System.out.println("                Invalid option. Please enter a number.              ");
                System.out.print(ConsoleColors.RESET);
                System.out.println("--------------------------------------------------------------------");
                scan.nextLine();
                userOption = -1;
            }

        } while (userOption != 6);
    }

    public void handleTransactionMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:
                handleTransferFunds(loggedInUser);
                break;
            case 2:
                handleShowAllTransactions(loggedInUser);
                break;
            case 3:
                handleShowTransactionsByDate(loggedInUser);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /*
      Handles the operation to transfer funds from one user to another.
      The user is guided to select an account, enter an amount, a message, and receiver's phone number.
      If the transfer operation fails due to reasons such as insufficient funds or invalid receiver,
      an error message is displayed to the console.
    */
    public void handleTransferFunds(User loggedInUser) {
        List<Account> allAccountsFromUser = accountService.getAllUserAccountsById(loggedInUser.getId());

        if (allAccountsFromUser.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.print(ConsoleColors.BLUE);
        System.out.println("                    Here are your accounts:                         ");
        System.out.print(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
        int i = 1;
        for (Account account : allAccountsFromUser) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.PURPLE);
            System.out.println("Account " + "(" + i + ")");
            System.out.println("Account name: " + account.getAccountName());
            System.out.println("Account number: " + account.getAccountNumber());
            System.out.print(ConsoleColors.RESET);
            i++;
        }

        int accountIndex = -1;
        while (accountIndex < 1 || accountIndex > allAccountsFromUser.size()) {
            System.out.println("Select the number corresponding to the account:");
            try {
                accountIndex = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.print(ConsoleColors.RED);
                System.out.println("--------------------------------------------------------------------");
                System.out.println("                Invalid input. Please enter a number.");
                System.out.println("--------------------------------------------------------------------");
                System.out.print(ConsoleColors.RESET);
                scan.nextLine();
            }
        }
        scan.nextLine();

        Account senderAccount = allAccountsFromUser.get(accountIndex - 1);

        BigDecimal amount;
        while (true) {
            System.out.println("Enter amount:");
            try {
                amount = scan.nextBigDecimal();
                scan.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        System.out.println("Enter message:");
        String description = scan.nextLine();
        System.out.println("Enter receivers phone number: ");
        String phone = scan.nextLine();
        User receiverUser;
        try {
            String validatedPhone = userService.validateReceiverPhone(phone);
            receiverUser = userService.getUserByPhone(validatedPhone, loggedInUser);
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.GREEN);
            System.out.println("                    Transaction successful!                         ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");

        } catch (UserNotFoundException | SameUserTransferException | IllegalArgumentException e) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println(e.getMessage());
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        Account receiverAccount = accountService.getDefaultAccountForUser(receiverUser.getId());

        try {
            transactionService.transferFunds(new Transaction(amount, description, senderAccount.getId(), receiverAccount.getId()), senderAccount);
        } catch (InsufficientFundsException e) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println(e.getMessage());
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        }
    }

    /*
      Fetches and displays all transactions associated with the logged-in user.
      If the user has no transactions, an appropriate message is displayed.
    */
    public void handleShowAllTransactions(User loggedInUser) {
        List<Transaction> transactions = transactionService.showAllTransactions(loggedInUser);
        if (transactions.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                You have no transactions                            ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.PURPLE);
            System.out.println("Date: " + transaction.getCreated());
            System.out.println("From accountId: " + transaction.getSenderAccountId() + "\nTo accountId: " + transaction.getReceiverAccountId());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.print(ConsoleColors.RESET);
        }
    }

    /*
      Fetches and displays transactions associated with the logged-in user, filtered by date.
      The user is guided to input the from-date and to-date for the transactions to be filtered.
      If the user has no transactions for the specified dates, an appropriate message is displayed.
    */
    public void handleShowTransactionsByDate(User loggedInUser) {
        LocalDate fromDate;
        LocalDate toDate;
        while (true) {
            System.out.println("From:");
            System.out.println("Enter the date (in the format yyyy-mm-dd):");
            String fromDateString = scan.nextLine();
            System.out.println("To:");
            System.out.println("Enter the date (in the format yyyy-mm-dd):");
            String toDateString = scan.nextLine();
            try {
                fromDate = LocalDate.parse(fromDateString);
                toDate = LocalDate.parse(toDateString);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        List<Transaction> transactions = transactionService.showTransactionsByDate(loggedInUser, fromDate, toDate);
        if (transactions.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                You have no transactions for this date              ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.PURPLE);
            System.out.println("Date: " + transaction.getCreated());
            System.out.println("From accountId: " + transaction.getSenderAccountId() + "\nTo accountId: " + transaction.getReceiverAccountId());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.print(ConsoleColors.RESET);
        }
    }
}
