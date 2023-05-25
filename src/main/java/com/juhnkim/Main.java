package com.juhnkim;

import com.juhnkim.repository.CreateDatabaseMySQL;

public class Main {
    public static void main(String[] args) {
        CreateDatabaseMySQL c = new CreateDatabaseMySQL();
        c.createUserTable();
        c.createAccountTable();
        c.createTransactionTable();
    }
}