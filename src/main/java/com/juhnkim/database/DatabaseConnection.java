package com.juhnkim.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    private static final DatabaseConnection db;

    private MysqlDataSource dataSource;
    private final String url;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private DatabaseConnection(String url, int port, String database, String username, String password) throws SQLException {
        this.url = url;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        initializeDataSource();
    }

    static {
        DatabaseConnection instance;
        try {
            Properties properties = new Properties();
            InputStream config = DatabaseConnection.class.getClassLoader().getResourceAsStream("swoshDBConfig.txt");
            properties.load(config);
            String url = properties.getProperty("url");
            int port = Integer.parseInt(properties.getProperty("port"));
            String database = properties.getProperty("database");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            instance = new DatabaseConnection(url, port, database, username, password);
        } catch (Exception e) {
            instance = null;
            System.err.println("Failed to create the DatabaseConnection instance: " + e.getMessage());
        }
        db = instance;
    }

    private void initializeDataSource() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
        dataSource.setUseSSL(false);
    }

    private Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to create a connection: " + e.getMessage());
            return null;
        }
    }

    public static Connection getConnection() {
        if (db == null) {
            throw new IllegalStateException("DatabaseConnection instance is not initialized");
        }
        return db.createConnection();
    }
}
