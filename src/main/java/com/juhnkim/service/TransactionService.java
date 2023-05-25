package com.juhnkim.service;

import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


}
