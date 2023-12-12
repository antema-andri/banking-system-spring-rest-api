package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.service.AppUserService;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<AppUserDTO> getUsers(){
        return appUserService.getAppUsers();
    }
}
