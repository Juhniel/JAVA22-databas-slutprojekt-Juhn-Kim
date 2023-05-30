package com.juhnkim.exception;

public class DeleteDefaultAccountException extends RuntimeException{
    public DeleteDefaultAccountException(){
        super("Cannot delete the default account, contact your bank for more information");
    }
}
