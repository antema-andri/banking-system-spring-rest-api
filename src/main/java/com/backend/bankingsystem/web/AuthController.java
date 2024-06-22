package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.service.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class AuthController {
    private final SecurityService securityService;

    @GetMapping("profile")
    public Authentication profile(Authentication authentication){
        return authentication;
    }

    @PostMapping("auth/token")
    public AppUserDTO login(@RequestBody AppUserDTO appUserDTO, HttpServletResponse response) {
        return securityService.authenticate(appUserDTO, response);
    }
}
