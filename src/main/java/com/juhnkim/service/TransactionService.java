package com.juhnkim.service;

import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean transferFunds(Transaction transaction) {

        // SE TILL ATT VERIFIERA TILLGÄNGLIGA BELOPP PÅ KONTON INNAN TRANSAKTIONER TILLÅTS
        return transactionRepository.transferFunds(transaction);
    }

    public List<Transaction> showAllTransactions(User loggedInUser) {
        return transactionRepository.showAllTransactions(loggedInUser);
    }

    public List<Transaction> showTransactionsByDate(User loggedInUser, LocalDate date) {
        return transactionRepository.showTransactionsByDate(loggedInUser, date);
    }


}
