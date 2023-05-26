package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.Account;
import com.juhnkim.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private void setPreparedStatementValues(PreparedStatement preparedStatement, Account account) throws SQLException {
        preparedStatement.setString(1, account.getAccountName());
        preparedStatement.setString(2, account.getAccountNumber());
        preparedStatement.setBigDecimal(3, account.getBalance());
        preparedStatement.setInt(4, account.getUserId());
    }


    public List<Account> getAccountById(int id) {
        String query = "SELECT * FROM account WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                int accountId = resultSet.getInt("id");
                String accountName = resultSet.getString("account_name");
                String accountNumber = resultSet.getString("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                int loggedInUserId = resultSet.getInt("user_id");

                accounts.add(new Account(accountId, accountName, accountNumber, balance, loggedInUserId));
            }
            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public List<Account> getAllUserAccounts(User user) {
        String query = "SELECT * FROM account WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                // Assuming you have a constructor in your Account class that takes id and balance as arguments
                Account account = new Account(id, balance);
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public boolean createBankAccount(User loggedInUser, Account account) {
        String query = "INSERT INTO account(account_name, account_number, balance, user_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setString(2, account.getAccountNumber());
            preparedStatement.setBigDecimal(3, account.getBalance());
            preparedStatement.setInt(4, loggedInUser.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public boolean deleteBankAccount(Account account) {
        String query = "DELETE FROM account WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, account.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

}
