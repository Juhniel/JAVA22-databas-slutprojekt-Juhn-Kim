package com.juhnkim.repository;

import com.juhnkim.database.DatabaseConnection;
import com.juhnkim.model.User;

import java.sql.*;

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

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String ssn = resultSet.getString("ssn");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        boolean online = resultSet.getBoolean("online");
        String phone = resultSet.getString("phone");
        String address = resultSet.getString("address");
        String password = resultSet.getString("password");

        return new User(id, name, ssn, email, online, phone, address, password);
    }

    private User getUserByParameter(String parameter, String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, parameter);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }

    public User getUserById(int id){
        String query = "SELECT * FROM user WHERE id = ?";
        return getUserByParameter(Integer.toString(id), query);
    }

    public User getUserBySsn(String ssn){
        String query = "SELECT * FROM user WHERE ssn = ?";
        if(ssn.contains("-")){
            ssn = ssn.replace("-", "");
        }
        return getUserByParameter(ssn, query);
    }

    public User getUserByPhone(String phone) {
        String query = "SELECT * FROM user WHERE phone = ?";
        return getUserByParameter(phone, query);
    }

    public User addUser(User user) {
        String query = "INSERT INTO user(ssn, name, email, online, phone, address, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setPreparedStatementValues(preparedStatement, user);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE user SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setPreparedStatementValuesForUpdate(preparedStatement, user);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    private void setPreparedStatementValuesForUpdate(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getAddress());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setInt(6, user.getId());
    }

    public boolean deleteUser(User user) {
        String query = "DELETE user FROM user WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}

