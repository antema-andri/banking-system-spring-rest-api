package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.AppRoleDTO;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.dto.InfoUserDTO;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.exceptions.ExistingUsernameException;
import com.backend.bankingsystem.exceptions.InvalidUserRoleException;
import com.backend.bankingsystem.service.AppUserService;
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

    @GetMapping("roles")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<AppRoleDTO> getUserRoles(){
        return appUserService.getRoles();
    }

    @PostMapping("users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public AppUserDTO createAppUser(@RequestBody InfoUserDTO infoUserDTO) throws InvalidUserRoleException, ExistingUsernameException, CustomerNotFoundException {
        return appUserService.createAppUser(infoUserDTO.getUsername(),infoUserDTO.getPassword(),infoUserDTO.getCustomerId(),infoUserDTO.getRoleId());
    }
}
