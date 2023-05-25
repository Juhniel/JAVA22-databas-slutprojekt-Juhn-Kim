package com.juhnkim.repository;

import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRepository {

    private void setPreparedStatementValues(PreparedStatement preparedStatement, Transaction transaction) throws SQLException {
        preparedStatement.setBigDecimal(1, transaction.getAmount());
        preparedStatement.setString(2, transaction.getTransactionType());
        preparedStatement.setString(3, transaction.getDescription());
        preparedStatement.setInt(4, transaction.getSenderAccountId());
        preparedStatement.setInt(5, transaction.getReceiverAccountId());
    }

    public void addBalanceToAccount(){

    }

    public void transferFunds() {

    }

    public void showAllTransactions() {

    }

    public void showTransactionsByDate() {

    }


}
