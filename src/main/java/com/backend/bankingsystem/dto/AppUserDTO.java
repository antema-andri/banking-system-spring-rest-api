package com.backend.bankingsystem.dto;

import lombok.Data;

@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private String password;
    private CustomerDTO customer;
    private AppRoleDTO appRole;
}
