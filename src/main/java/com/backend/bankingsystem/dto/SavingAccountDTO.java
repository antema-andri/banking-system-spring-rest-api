package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class SavingAccountDTO extends BankAccountDTO{
    private double interestRate;
}
