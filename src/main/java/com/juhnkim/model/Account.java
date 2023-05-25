package com.juhnkim.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private int id;
    private Timestamp created;
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
    private int userId;

    public Account(int id, Timestamp created, String accountName, String accountNumber, BigDecimal balance, int userId) {
        this.id = id;
        this.created = created;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
