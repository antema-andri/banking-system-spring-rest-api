package com.backend.bankingsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class BankAccountPageDTO {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<BankAccountDTO> accounts;
}
