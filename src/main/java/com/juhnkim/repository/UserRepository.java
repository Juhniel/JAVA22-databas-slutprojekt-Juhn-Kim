package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public User getUserById(int id){
        String query = "SELECT * FROM user WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String ssn = resultSet.getString("ssn");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                boolean online = resultSet.getBoolean("online");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");
                User user = new User(name, ssn, email, online, phone, address, password);
                user.setId(id);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }

    public User getUserBySsn(String ssn){
        String query = "SELECT * FROM user WHERE ssn = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ssn);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                boolean online = resultSet.getBoolean("online");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");
                User user = new User(name, ssn, email, online, phone, address, password);
                user.setSsn(ssn);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }
}
