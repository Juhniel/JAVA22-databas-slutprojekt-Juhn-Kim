package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.Account;
import com.juhnkim.model.Transaction;
import com.juhnkim.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private final AccountRepository accountRepository;

    public TransactionRepository(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    private void setTransactionPreparedStatement(PreparedStatement preparedStatement, Transaction transaction, Account senderAccount) throws SQLException {
        preparedStatement.setBigDecimal(1, transaction.getAmount());
        preparedStatement.setString(2, transaction.getDescription());
        preparedStatement.setInt(3, senderAccount.getId());
        preparedStatement.setInt(4, transaction.getReceiverAccountId());
    }

    public boolean addTransaction(Transaction transaction, Account senderAccount) {
        String query = "INSERT INTO transaction(amount, description, sender_account_id, receiver_account_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setTransactionPreparedStatement(preparedStatement, transaction, senderAccount);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public boolean transferFunds(Transaction transaction, Account senderAccount) {
        String query1 = "UPDATE account SET balance = balance - ? WHERE id = ?";
        String query2 = "UPDATE account SET balance = balance + ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(query2)) {

            setTransactionPreparedStatement(preparedStatement1, transaction, senderAccount);
            preparedStatement1.executeUpdate();

            Account defaultAccount = accountRepository.getDefaultAccountForUser(senderAccount.getUserId());
            int receiverUserId = defaultAccount.getId();

            setTransactionPreparedStatement(preparedStatement2, transaction, senderAccount);
            int rowsAffected = preparedStatement2.executeUpdate();

            addTransaction(transaction, senderAccount);

            return rowsAffected > 0;

        } catch (Exception e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    private List<Transaction> createTransactionList(ResultSet resultSet) throws SQLException {
        List<Transaction> transactionList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Timestamp created = resultSet.getTimestamp("created");
            BigDecimal amount = resultSet.getBigDecimal("amount");
            String description = resultSet.getString("description");
            int senderAccountId = resultSet.getInt("sender_account_id");
            int receiverAccountId = resultSet.getInt("receiver_account_id");
            transactionList.add(new Transaction(id, created, amount, description, senderAccountId, receiverAccountId));
        }
        return transactionList;
    }

    public List<Transaction> showAllTransactions(User user) {
        String query = "SELECT * FROM transaction WHERE sender_account_id IN (SELECT id FROM account WHERE user_id = ?) OR receiver_account_id IN (SELECT id FROM account WHERE user_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            return createTransactionList(resultSet);

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

            return createTransactionList(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
