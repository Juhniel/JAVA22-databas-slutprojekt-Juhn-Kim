package com.juhnkim.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public void createUserTable() {
        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "ssn VARCHAR(13)," +
                    "name VARCHAR(50), " +
                    "email VARCHAR(50) UNIQUE, " +
                    "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "online BOOLEAN, " +
                    "phone VARCHAR(20), " +
                    "address VARCHAR(100), " +
                    "password VARCHAR(255)" +
                    ")";

            statement.executeUpdate(query);
            System.out.println("user table successfully created!");

        } catch (Exception e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void createAccountTable() {
        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS account (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "account_name VARCHAR(100), " +
                    "account_number VARCHAR(100), " +
                    "balance DECIMAL(19, 2)," +
                    "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "is_default BOOLEAN DEFAULT FALSE," +
                    "user_id INT, " +
                    "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE)";

            statement.executeUpdate(query);
            System.out.println("account table successfully created!");

        } catch (Exception e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void createTransactionTable() {
        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS transaction (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "amount DECIMAL(19, 2), " +
                    "description VARCHAR(255), " +
                    "sender_account_id INT, " +
                    "receiver_account_id INT, " +
                    "FOREIGN KEY (sender_account_id) REFERENCES account(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (receiver_account_id) REFERENCES account(id) ON DELETE CASCADE)";

            statement.executeUpdate(query);
            System.out.println("transaction table successfully created!");

        } catch (Exception e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }
}


