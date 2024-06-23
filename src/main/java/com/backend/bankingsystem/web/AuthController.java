package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.service.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class AuthController {
    private final SecurityService securityService;

    @PostMapping("auth/token")
    public AppUserDTO login(@RequestBody AppUserDTO appUserDTO, HttpServletResponse response) {
        return securityService.authenticate(appUserDTO, response);
    }
}
