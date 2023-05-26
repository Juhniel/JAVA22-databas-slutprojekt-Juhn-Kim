package com.juhnkim.service;

import com.juhnkim.model.Account;
import com.juhnkim.model.User;
import com.juhnkim.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    public List<Account> getAccountById(int id) {
        return accountRepository.getAccountById(id);
    }

    public List<Account> getAllUserAccounts(User loggedInUser) {
        return accountRepository.getAllUserAccounts(loggedInUser);
    }

    public boolean createBankAccount(User loggedInUser, String accountName) {
        return accountRepository.createBankAccount(loggedInUser, new Account(accountName, loggedInUser.getId()));
    }

    public boolean deleteBankAccount(Account userAccounts) {
        return accountRepository.deleteBankAccount(userAccounts);
    }
}
