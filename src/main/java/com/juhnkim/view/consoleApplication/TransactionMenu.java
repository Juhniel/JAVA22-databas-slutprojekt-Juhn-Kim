package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.service.AccountService;
import com.juhnkim.service.TransactionService;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransactionMenu {

    private final AccountService accountService;
    private final TransactionService transactionService;

    private final Scanner scan;

    public TransactionMenu(AccountService accountService, TransactionService transactionService, Scanner scan) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.scan = scan;
    }

    public void displayTransactionMenu(User loggedInUser) {
        int userOption;
        do {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("                        1. Transfer funds                           ");
            System.out.println("                        2. Show all transactions                    ");
            System.out.println("                        3. Show transactions by date                ");
            System.out.println("                        6. Previous                                 ");
            System.out.println("--------------------------------------------------------------------");

            userOption = scan.nextInt();
            scan.nextLine();
            handleTransactionMenu(userOption, loggedInUser);
        } while (userOption != 6);
    }

    public void handleTransactionMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:

                break;
            case 2:
//                transactionService.showAllTransactions();
                break;
            case 3:
//                transactionService.showTransactionsByDate();
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    public void handleTransferFunds() {
        System.out.println("Select your bank account:");
        int senderAccountId = scan.nextInt();
        scan.nextLine();
        System.out.println("Enter amount:");
        BigDecimal amount = scan.nextBigDecimal();
        System.out.println("Enter message:");
        String description = scan.nextLine();
        System.out.println("Enter transaction type:");
        String transactionType = scan.nextLine();
        System.out.println("Enter receivers bank number: ");
        int receiverAccountId = scan.nextInt();
        scan.nextLine();
        transactionService.transferFunds(new Transaction(amount, transactionType, description, senderAccountId, receiverAccountId));
    }

    public void handleShowAllTransactions() {

    }

    public void handleShowTransactionsByDate() {

    }


}
