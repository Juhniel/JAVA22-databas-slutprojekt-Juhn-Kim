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
import java.util.UUID;

public class AccountRepository {
    private void setPreparedStatementValues(PreparedStatement preparedStatement, Account account) throws SQLException {
        preparedStatement.setString(1, account.getAccountName());
        preparedStatement.setString(2, account.getAccountNumber());
        preparedStatement.setBigDecimal(3, account.getBalance());
        preparedStatement.setInt(4, account.getUserId());
    }


    public List<Account> getAllUserAccountsById(int id) {
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
                boolean isDefault = resultSet.getBoolean("is_default");
                int loggedInUserId = resultSet.getInt("user_id");

                accounts.add(new Account(accountId, accountName, accountNumber, balance, isDefault, loggedInUserId));
            }
            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public Account getDefaultAccountForUser(int userId) {
        String query = "SELECT * FROM account WHERE user_id = ? AND is_default = TRUE";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String accountName = resultSet.getString("account_name");
                String accountNumber = resultSet.getString("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                boolean isDefault = resultSet.getBoolean("is_default");

                return new Account(id, accountName, accountNumber, balance, isDefault, userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }


    public boolean createBankAccount(User loggedInUser, Account account) {
        String query = "INSERT INTO account(account_name, account_number, balance, is_default, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String accountNumber = UUID.randomUUID().toString();
            account.setAccountNumber(accountNumber);

            preparedStatement.setString(1, account.getAccountName());
            preparedStatement.setString(2, account.getAccountNumber());
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(1000));
            preparedStatement.setBoolean(4, account.isDefault());
            preparedStatement.setInt(5, loggedInUser.getId());

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
