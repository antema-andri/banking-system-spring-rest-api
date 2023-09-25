package com.backend.bankingsystem.exceptions;

public class CustomerNotFoundException extends Exception{

    public CustomerNotFoundException(String message){
        super(message);
    }
}
