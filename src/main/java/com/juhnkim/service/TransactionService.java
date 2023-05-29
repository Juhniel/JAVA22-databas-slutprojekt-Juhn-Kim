package com.juhnkim.service;

import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public boolean transferFunds(Transaction transaction, User loggedInUser) {
        // Get the sender's user ID
        int senderUserId = transaction.getSenderAccountId();

        System.out.println("Sender accountId:" + senderUserId);

        // Get the sender's default account
        Account senderAccount = accountRepository.getDefaultAccountForUser(loggedInUser.getId());


        if (senderAccount == null) {
            throw new RuntimeException("Sender account not found");
        }

        // Check if the sender has enough funds
        if (senderAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds for the transaction.");
        }

        // If the sender has enough funds, execute the transaction
        return transactionRepository.transferFunds(transaction);
    }

    public List<Transaction> showAllTransactions(User loggedInUser) {
        return transactionRepository.showAllTransactions(loggedInUser);
    }

    public List<Transaction> showTransactionsByDate(User loggedInUser, LocalDate date) {
        return transactionRepository.showTransactionsByDate(loggedInUser, date);
    }
}
