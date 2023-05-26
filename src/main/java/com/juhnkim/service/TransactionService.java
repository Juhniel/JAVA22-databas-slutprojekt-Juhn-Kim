package com.juhnkim.service;

import com.juhnkim.model.Transaction;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;

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

    public List<Transaction> showAllTransactions() {
        return transactionRepository.showAllTransactions();
    }

    public List<Transaction> showTransactionsByDate() {
        return transactionRepository.showTransactionsByDate();
    }


}
