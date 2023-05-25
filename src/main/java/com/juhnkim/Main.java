package com.juhnkim;

import com.juhnkim.database.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer c = new DatabaseInitializer();
        c.createUserTable();
        c.createAccountTable();
        c.createTransactionTable();
    }
}