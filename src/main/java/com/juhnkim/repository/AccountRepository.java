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

    private Account createAccountFromResultSet(ResultSet resultSet, int userId) throws SQLException {
        int id = resultSet.getInt("id");
        String accountName = resultSet.getString("account_name");
        String accountNumber = resultSet.getString("account_number");
        BigDecimal balance = resultSet.getBigDecimal("balance");
        boolean isDefault = resultSet.getBoolean("is_default");

        return new Account(id, accountName, accountNumber, balance, isDefault, userId);
    }

    public List<Account> getAllUserAccountsById(int id) {
        String query = "SELECT * FROM account WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(createAccountFromResultSet(resultSet, id));
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
                return createAccountFromResultSet(resultSet, userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }

    private void setCreateBankAccountPreparedStatement(PreparedStatement preparedStatement, User loggedInUser, Account account) throws SQLException {
        String accountNumber = UUID.randomUUID().toString();
        account.setAccountNumber(accountNumber);

        preparedStatement.setString(1, account.getAccountName());
        preparedStatement.setString(2, account.getAccountNumber());
        preparedStatement.setBigDecimal(3, BigDecimal.valueOf(1000));
        preparedStatement.setBoolean(4, account.isDefault());
        preparedStatement.setInt(5, loggedInUser.getId());
    }

    public boolean createBankAccount(User loggedInUser, Account account) {
        String query = "INSERT INTO account(account_name, account_number, balance, is_default, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setCreateBankAccountPreparedStatement(preparedStatement, loggedInUser, account);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void deleteBankAccount(Account account) {
        String query = "DELETE account FROM account WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, account.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
