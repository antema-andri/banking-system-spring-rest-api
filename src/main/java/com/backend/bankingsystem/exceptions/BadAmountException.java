package com.backend.bankingsystem.exceptions;

public class BadAmountException extends Exception {
    public BadAmountException(String message) {
        super(message);
    }
}
