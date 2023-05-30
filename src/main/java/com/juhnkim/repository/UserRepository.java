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

    public User getUserById(int id){
        String query = "SELECT * FROM user WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int user_id = resultSet.getInt("id");
                String ssn = resultSet.getString("ssn");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                boolean online = resultSet.getBoolean("online");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");
                User user = new User(user_id, name, ssn, email, online, phone, address, password);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }

    public User getUserBySsn(String ssn){
        String query = "SELECT * FROM user WHERE ssn = ?";

        if(ssn.contains("-")){
            ssn = ssn.replace("-", "");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ssn);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                boolean online = resultSet.getBoolean("online");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");
                User user = new User(id, name, ssn, email, online, phone, address, password);
                user.setSsn(ssn);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
        return null;
    }

    public User getUserByPhone(String phone) {
        String query = "SELECT * FROM user WHERE phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int user_id = resultSet.getInt("id");
                String ssn = resultSet.getString("ssn");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                boolean online = resultSet.getBoolean("online");
                String userPhone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");

                User user = new User(user_id, name, ssn, email, online, userPhone, address, password);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }

        return null;
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

    public boolean updateUser(User user) {
        String query = "UPDATE user SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementValues(preparedStatement, user);
            preparedStatement.setInt(8, user.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public boolean deleteUser(User user) {
        String query1 = "UPDATE account SET account_name = 'Deleted Account', is_deleted = TRUE, deleted_at = NOW() WHERE user_id = ?";
        String query2 = "UPDATE user SET ssn = NULL, name = 'Deleted User', email = 'deleted@email.com', phone = NULL, address = NULL, password = NULL, is_deleted = TRUE, deleted_at = NOW() WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(query2)) {

            // Set all bank accounts associated with this user as deleted
            preparedStatement1.setInt(1, user.getId());
            preparedStatement1.executeUpdate();

            // Set the user as deleted and anonymize user's personal data
            preparedStatement2.setInt(1, user.getId());
            int rowsAffected = preparedStatement2.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }

    }
}
