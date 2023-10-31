package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class TransferDTO {
    private String accountSourceId;
    private String accountDestinationId;
    private double amount;
    private String desc;
}
