package com.juhnkim.service;

import com.juhnkim.exception.InsufficientFundsException;
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

    public boolean transferFunds(Transaction transaction, Account senderAccount) {

//        Account senderAccount = accountService.getDefaultAccountForUser(loggedInUser.getId());

//        if (loggedInUser == null) {
//            throw new RuntimeException("Sender account not found");
//        }

        BigDecimal transactionAmount = transaction.getAmount().setScale(2, RoundingMode.HALF_UP);

        // Check if the sender has enough funds
        if (senderAccount.getBalance().compareTo(transactionAmount) < 0) {
            throw new InsufficientFundsException(senderAccount.getBalance(), transactionAmount);
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
