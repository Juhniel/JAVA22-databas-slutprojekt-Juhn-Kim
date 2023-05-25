package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;

import java.sql.Connection;
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

    public void addBalanceToAccount(Transaction transaction){
        String query = "UPDATE account SET balance = balance + ? WHERE account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, transaction.getAmount());
            preparedStatement.setInt(2, transaction.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void transferFunds(Transaction transaction) {
        String query1 = "UPDATE account SET balance = balance - ? WHERE account_number = ?";
        String query2 = "UPDATE account SET balance = balance + ? WHERE account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(query2)) {

            // start transaction
            connection.setAutoCommit(false);

            // subtract amount from sender's account
            preparedStatement1.setBigDecimal(1, transaction.getAmount());
            preparedStatement1.setInt(2, transaction.getSenderAccountId());
            preparedStatement1.executeUpdate();

            // add amount to receiver's account
            preparedStatement2.setBigDecimal(1, transaction.getAmount());
            preparedStatement2.setInt(2, transaction.getReceiverAccountId());
            preparedStatement2.executeUpdate();

            // end transaction
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }


    public void showAllTransactions() {

    }

    public void showTransactionsByDate() {

    }


}
