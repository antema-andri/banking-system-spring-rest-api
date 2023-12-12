package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService{
    private final EntityMapper entityMapper;
    private final AppUserRepository appUserRepository;

    @Override
    public AppUserDTO getCurrentUser(String username) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppUserDTO appUserDTO=entityMapper.fromEntity(appUser);
        return appUserDTO;
    }

    @Override
    public List<AppUserDTO> getAppUsers(){
        List<AppUser> appUsers=appUserRepository.findAll();
        appUsers.forEach(user->{
            System.out.println(entityMapper.fromEntity(user).getId());
        });
        return appUsers.stream().map(appUser->entityMapper.fromEntity(appUser)).collect(Collectors.toList());
    }
}
