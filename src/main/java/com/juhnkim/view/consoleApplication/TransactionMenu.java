package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.repository.UserRepository;
import com.juhnkim.service.AccountService;
import com.juhnkim.service.TransactionService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TransactionMenu {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;

    private final Scanner scan;


    public TransactionMenu(UserRepository userRepository, AccountService accountService, TransactionService transactionService, Scanner scan) {
        this.userRepository = userRepository;
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

    public void handleTransferFunds(User loggedInUser) {
        List<Account> allAccountsFromUser = accountService.getAllUserAccountsById(loggedInUser.getId());

        if(allAccountsFromUser.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        System.out.println("Here are your accounts:");
        int i = 1;
        for(Account account : allAccountsFromUser) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Account " + i);
            System.out.println("Account name: " + account.getAccountName());
            System.out.println("Account number: " + account.getAccountNumber());
            System.out.println("--------------------------------------------------------------------");
            i++;
        }

        int accountIndex = -1;
        while(accountIndex < 1 || accountIndex > allAccountsFromUser.size()) {
            System.out.println("Select the number corresponding to the account:");
            try {
                accountIndex = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scan.nextLine(); // clear the invalid input
            }
        }
        scan.nextLine(); // clear the line

        Account senderAccount = allAccountsFromUser.get(accountIndex - 1);

        System.out.println("Enter amount:");
        BigDecimal amount = scan.nextBigDecimal();
        scan.nextLine();
        System.out.println("Enter message:");
        String description = scan.nextLine();
        System.out.println("Enter receivers phone number: ");
        String phone = scan.nextLine();
        User receiverUser = userRepository.getUserByPhone(phone);
        Account receiverAccount = accountService.getDefaultAccountForUser(receiverUser.getId());

        if (receiverAccount == null) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("             The phone number you entered \n does not match any user.  ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }
        System.out.println("DU SKICKAR FRÅN: " + senderAccount.getId());
        System.out.println("DU SKICKAR TILL " + receiverAccount.getId());

        transactionService.transferFunds(new Transaction(amount, description, senderAccount.getId(), receiverAccount.getId()), loggedInUser);
    }

    public void handleShowAllTransactions(User loggedInUser) {
        List<Transaction> transactions = transactionService.showAllTransactions(loggedInUser);
        if(transactions.isEmpty()){
            System.out.println("You have no transactions.");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println("Date: " + transaction.getCreated());
            System.out.println("From: " + userRepository.getUserById(transaction.getSenderAccountId()).getName());
            System.out.println("To: " + userRepository.getUserById(transaction.getReceiverAccountId()).getName());
            System.out.println("Amount: " + transaction.getAmount());
        }
    }

    public void handleShowTransactionsByDate(User loggedInUser) {
        System.out.println("Enter the date (in the format yyyy-mm-dd):");
        String dateInput = scan.nextLine();
        LocalDate date = LocalDate.parse(dateInput);
        List<Transaction> transactions = transactionService.showTransactionsByDate(loggedInUser, date);
        if(transactions.isEmpty()){
            System.out.println("You have no transactions for this date.");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }


}
