package com.juhnkim.service;

import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public boolean transferFunds(Transaction transaction, User loggedInUser) {

        // Get the sender's default account
        Account senderAccount = accountService.getDefaultAccountForUser(loggedInUser.getId());

        if (senderAccount == null) {
            throw new RuntimeException("Sender account not found");
        }


        BigDecimal transactionAmount = transaction.getAmount().setScale(2, RoundingMode.HALF_UP);

        // Check if the sender has enough funds
        if (senderAccount.getBalance().compareTo(transactionAmount) < 0) {
            System.out.println("--------------------------------------------------------------------");
            System.out.print(ConsoleColors.RED);
            System.out.println("                Insufficient funds on your account                  ");
            System.out.print(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
            return false;
        }

        // If the sender has enough funds, execute the transaction
        return transactionRepository.transferFunds(transaction, senderAccount);
    }

    public List<Transaction> showAllTransactions(User loggedInUser) {
        return transactionRepository.showAllTransactions(loggedInUser);
    }

    public List<Transaction> showTransactionsByDate(User loggedInUser, LocalDate date) {
        return transactionRepository.showTransactionsByDate(loggedInUser, date);
    }
}
