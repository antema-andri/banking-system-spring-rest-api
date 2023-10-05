package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.model.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AppAccountServiceImpl implements AppAccountService{
    private final AppUserRepository appUserRepository;

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
