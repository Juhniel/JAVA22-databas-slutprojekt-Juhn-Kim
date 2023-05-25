package com.juhnkim.repository;

import com.juhnkim.model.Account;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountRepository {
    private void setPreparedStatementValues(PreparedStatement preparedStatement, Account account) throws SQLException {
        preparedStatement.setString(1, account.getAccountName());
        preparedStatement.setString(2, account.getAccountNumber());
        preparedStatement.setBigDecimal(3, account.getBalance());
        preparedStatement.setInt(4, account.getUserId());
    }

    public void addBankAccount() {

    }

    public void deleteBankAccount() {

    }
    public void checkBalance() {

    }


}
