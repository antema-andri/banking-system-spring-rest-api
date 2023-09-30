package com.backend.bankingsystem.dto;

import com.backend.bankingsystem.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customer;
    private String type;
}
