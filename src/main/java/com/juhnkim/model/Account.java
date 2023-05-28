package com.juhnkim.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private int id;
    private Timestamp created;
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
    private boolean isDefault;
    private int userId;

    public Account() {

    }

    public Account(String accountName, int userId) {
        this.accountName = accountName;
        this.userId = userId;
    }

    public Account(int id, String accountName, String accountNumber, BigDecimal balance, boolean isDefault, int userId) {
        this.id = id;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isDefault = isDefault;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
