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
        List<Account> allAccountsFromUser = accountService.getAccountById(loggedInUser.getId());

        if(allAccountsFromUser.isEmpty()) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("                  You don't have any accounts.                       ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        System.out.println("Here are your accounts:");
        int index = 1;
        for(Account account : allAccountsFromUser) {
            System.out.println(index + ". " + account.getAccountName() + " " + account.getAccountNumber());
            index++;
        }
        System.out.println("Select your bank account:");
        int accountIndex = scan.nextInt();
        scan.nextLine();

        Account senderAccount = allAccountsFromUser.get(accountIndex - 1);

        System.out.println("Enter amount:");
        BigDecimal amount = scan.nextBigDecimal();
        scan.nextLine();
        System.out.println("Enter message:");
        String description = scan.nextLine();
        System.out.println("Enter transaction type:");
        String transactionType = scan.nextLine();
        System.out.println("Enter receivers phone number: ");
        String phone = scan.nextLine();
        User receiverUser = userRepository.getUserByPhone(phone);

        if (receiverUser == null) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println(ConsoleColors.RED);
            System.out.println("             The phone number you entered does not match any user.  ");
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return;
        }

        transactionService.transferFunds(new Transaction(amount, transactionType, description, senderAccount.getId(), receiverUser.getId()));
    }

    public void handleShowAllTransactions(User loggedInUser) {
        List<Transaction> transactions = transactionService.showAllTransactions(loggedInUser);
        if(transactions.isEmpty()){
            System.out.println("You have no transactions.");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
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
