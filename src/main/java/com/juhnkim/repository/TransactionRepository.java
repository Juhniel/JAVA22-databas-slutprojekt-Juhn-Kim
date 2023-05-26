package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;

import java.math.BigDecimal;
import java.sql.*;
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

//    public void addBalanceToAccount(Transaction transaction){
//        String query = "UPDATE account SET balance = balance + ? WHERE account_number = ?";
//
//        try (Connection connection = DatabaseConnection.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setBigDecimal(1, transaction.getAmount());
//            preparedStatement.setInt(2, transaction.getId());
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Database operation failed", e);
//        }
//    }

    public boolean transferFunds(Transaction transaction) {
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
            int rowsAffected = preparedStatement2.executeUpdate();


            // end transaction
            connection.commit();
            connection.setAutoCommit(true);
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }


    public List<Transaction> showAllTransactions() {
        String query = "SELECT * FROM transaction";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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


    public List<Transaction> showTransactionsByDate() {
        String query = "SELECT * FROM transaction ORDER BY created DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

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
