package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.AppUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EntityMapper entityMapper;

    @Override
    public AppUserDTO authenticate(AppUserDTO appUserDTO, HttpServletResponse response) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUserDTO.getUsername(), appUserDTO.getPassword())
        );
        String token=tokenService.generateToken(authentication);
        AppUser appUser=appUserRepository.findByUsername(appUserDTO.getUsername());

        response.addHeader("Authorization", "Bearer " + token);
        return entityMapper.fromEntity(appUser);
    }

}
