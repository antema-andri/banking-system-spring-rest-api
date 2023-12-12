package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.AppUserDTO;

import java.util.List;

public interface AppUserService {
    AppUserDTO getCurrentUser(String username);

    List<AppUserDTO> getAppUsers();
}
