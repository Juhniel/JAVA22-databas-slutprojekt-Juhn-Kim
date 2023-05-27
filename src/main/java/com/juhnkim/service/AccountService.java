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

    public boolean createBankAccount(User loggedInUser, String accountName) {
        Account account = new Account();
        account.setAccountName(accountName);
        return accountRepository.createBankAccount(loggedInUser, account);
    }

    public boolean deleteBankAccount(Account userAccounts) {
        return accountRepository.deleteBankAccount(userAccounts);
    }
}
