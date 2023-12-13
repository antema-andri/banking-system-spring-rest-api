package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class InfoUserDTO {
    private String username;
    private String password;
    private String roleId;
    private String customerId;
}
