package com.backend.bankingsystem.dto;

import com.backend.bankingsystem.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class SavingAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double interestRate;
}
