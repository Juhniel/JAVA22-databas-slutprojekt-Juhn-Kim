package com.juhnkim.exception;

public class SameUserTransferException extends RuntimeException {
    public SameUserTransferException() {
        super("You cannot transfer funds to your own account.");
    }
}
