package com.juhnkim;

import com.juhnkim.database.DatabaseInitializer;
import com.juhnkim.repository.AccountRepository;
import com.juhnkim.repository.TransactionRepository;
import com.juhnkim.repository.UserRepository;
import com.juhnkim.service.*;
import com.juhnkim.view.Swosh;
import com.juhnkim.view.consoleApplication.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Swosh swosh = new Swosh();
        swosh.initializeSQLTables();
        swosh.runConsoleApplication();
    }
}