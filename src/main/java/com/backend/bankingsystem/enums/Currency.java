package com.backend.bankingsystem.enums;

public enum Currency {
    EURO("EUR"),
    DOLLAR("USD");

    private String code;

    private Currency(String code){
        this.code=code;
    }

    public String getCode(){
        return this.code;
    }
}
