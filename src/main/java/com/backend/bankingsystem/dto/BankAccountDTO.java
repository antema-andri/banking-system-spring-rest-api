package com.backend.bankingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
}
