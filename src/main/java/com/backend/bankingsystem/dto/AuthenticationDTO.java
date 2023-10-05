package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String username;
    private String password;
}
