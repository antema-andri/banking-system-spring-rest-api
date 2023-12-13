package com.backend.bankingsystem.exceptions;

public class ExistingUsernameException extends Exception{

    public ExistingUsernameException(String message){
        super(message);
    }
}
