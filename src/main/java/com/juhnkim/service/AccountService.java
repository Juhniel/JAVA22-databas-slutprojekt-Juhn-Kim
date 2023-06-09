package com.juhnkim.service;

import com.juhnkim.exception.DeleteDefaultAccountException;
import com.juhnkim.model.Account;
import com.juhnkim.model.User;
import com.juhnkim.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getDefaultAccountForUser(int id){
        return accountRepository.getDefaultAccountForUser(id);
    }

    public List<Account> getAllUserAccountsById(int id) {
        return accountRepository.getAllUserAccountsById(id);
    }

    public boolean createBankAccount(User loggedInUser, String accountName) {
        Account account = new Account();
        account.setAccountName(accountName);
        List<Account> allAccountsFromUser = accountRepository.getAllUserAccountsById(loggedInUser.getId());
        boolean hasDefaultAccount = allAccountsFromUser.stream().anyMatch(Account::isDefault);

        if (!hasDefaultAccount) {
            account.setDefault(true);
        } else {
            account.setDefault(false);
        }
        return accountRepository.createBankAccount(loggedInUser, account);
    }

    public void deleteBankAccount(Account accountToDelete, List<Account> allAccountsFromUser) {
        if(accountToDelete.isDefault() && allAccountsFromUser.size() > 1){
            throw new DeleteDefaultAccountException();
        }
        accountRepository.deleteBankAccount(accountToDelete);
    }

}
