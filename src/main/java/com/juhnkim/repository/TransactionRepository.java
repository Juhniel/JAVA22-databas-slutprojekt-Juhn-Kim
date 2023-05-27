package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private void setPreparedStatementValues(PreparedStatement preparedStatement, Transaction transaction) throws SQLException {
        preparedStatement.setBigDecimal(1, transaction.getAmount());
        preparedStatement.setString(2, transaction.getTransactionType());
        preparedStatement.setString(3, transaction.getDescription());
        preparedStatement.setInt(4, transaction.getSenderAccountId());
        preparedStatement.setInt(5, transaction.getReceiverAccountId());
    }

    public boolean transferFunds(Transaction transaction) {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        String query1 = "UPDATE account SET balance = balance - ? WHERE id = ?";
        String query2 = "UPDATE account SET balance = balance + ? WHERE id = ?";

        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);

            // start transaction
            connection.setAutoCommit(false);

            // subtract amount from sender's account
            preparedStatement1.setBigDecimal(1, transaction.getAmount());
            preparedStatement1.setInt(2, transaction.getSenderAccountId());
            preparedStatement1.executeUpdate();

            // add amount to receiver's account
            preparedStatement2.setBigDecimal(1, transaction.getAmount());
            preparedStatement2.setInt(2, transaction.getReceiverAccountId());
            int rowsAffected = preparedStatement2.executeUpdate();

            // end transaction
            connection.commit();
            connection.setAutoCommit(true);
            return rowsAffected > 0;

        } catch (SQLException e) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
            throw new RuntimeException("Database operation failed", e);
        } finally {
            if (preparedStatement1 != null) {
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement2 != null) {
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public List<Transaction> showAllTransactions(User user) {
        String query = "SELECT * FROM transaction WHERE sender_account_id IN (SELECT id FROM account WHERE user_id = ?) OR receiver_account_id IN (SELECT id FROM account WHERE user_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Transaction> transactionList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Timestamp created = resultSet.getTimestamp("created");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String transactionType = resultSet.getString("transaction_type");
                String description = resultSet.getString("description");
                int senderAccountId = resultSet.getInt("sender_account_id");
                int receiverAccountId = resultSet.getInt("receiver_account_id");
                transactionList.add(new Transaction(id, created, amount, transactionType, description, senderAccountId, receiverAccountId));
            }
            return transactionList;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }


    public List<Transaction> showTransactionsByDate(User user, LocalDate date) {
        String query = "SELECT * FROM transaction WHERE (sender_account_id IN (SELECT id FROM account WHERE user_id = ?) OR receiver_account_id IN (SELECT id FROM account WHERE user_id = ?)) AND DATE(created) = ? ORDER BY created DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Transaction> transactionList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Timestamp created = resultSet.getTimestamp("created");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String transactionType = resultSet.getString("transaction_type");
                String description = resultSet.getString("description");
                int senderAccountId = resultSet.getInt("sender_account_id");
                int receiverAccountId = resultSet.getInt("receiver_account_id");
                transactionList.add(new Transaction(id, created, amount, transactionType, description, senderAccountId, receiverAccountId));
            }
            return transactionList;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
