package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class CurrentAccountDTO extends BankAccountDTO{
    private double overDraft;
}
