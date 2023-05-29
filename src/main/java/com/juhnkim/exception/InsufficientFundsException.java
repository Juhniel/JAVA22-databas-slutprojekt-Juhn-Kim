package com.juhnkim.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(BigDecimal balance, BigDecimal requiredAmount) {
        super("Insufficient funds on your account. Current balance: " + balance + ". Required amount: " + requiredAmount + ".");
    }
}
