package com.backend.bankingsystem.service;

import com.backend.bankingsystem.model.AppUser;

public interface AppAccountService {
    AppUser loadUserByUsername(String login);
}
