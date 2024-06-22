package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.AppUserDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {
    AppUserDTO authenticate(AppUserDTO appUserDTO, HttpServletResponse response);
}
