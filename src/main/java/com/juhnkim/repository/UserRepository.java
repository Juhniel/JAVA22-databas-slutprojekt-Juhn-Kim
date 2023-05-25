package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepository {

    private void setPreparedStatementValues(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getSsn());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setBoolean(4, user.isOnline());
        preparedStatement.setString(5, user.getPhone());
        preparedStatement.setString(6, user.getAddress());
        preparedStatement.setString(7, user.getPassword());
    }

    public void addUser(User user) {
        String query = "INSERT INTO user(ssn, name, email, online, phone, address, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setPreparedStatementValues(preparedStatement, user);
            preparedStatement.executeUpdate();
            System.out.println("User added!");

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
