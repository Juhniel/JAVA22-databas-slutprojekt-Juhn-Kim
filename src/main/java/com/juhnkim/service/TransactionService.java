package com.juhnkim.service;

import com.juhnkim.exception.InsufficientFundsException;
import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;


/*
    This class validates the users inputs before passing them to the repository classes where we handle methods to
    the mySQL database. Validating user inputs and catching error.
*/


public class TransactionService {
    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void transferFunds(Transaction transaction, Account senderAccount) {
        BigDecimal transactionAmount = transaction.getAmount().setScale(2, RoundingMode.HALF_UP);

        if (senderAccount.getBalance().compareTo(transactionAmount) < 0) {
            throw new InsufficientFundsException(senderAccount.getBalance(), transactionAmount);
        }
        transactionRepository.transferFunds(transaction, senderAccount);
    }

    public List<Transaction> showAllTransactions(User loggedInUser) {
        return transactionRepository.showAllTransactions(loggedInUser);
    }

    public List<Transaction> showTransactionsByDate(User loggedInUser, LocalDate fromDate, LocalDate toDate) {
        return transactionRepository.showTransactionsByDate(loggedInUser, fromDate, toDate);
    }
}
