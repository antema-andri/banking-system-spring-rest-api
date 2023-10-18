package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.AppUserDTO;

public interface AppUserService {
    AppUserDTO getCurrentUser(String username);
}
