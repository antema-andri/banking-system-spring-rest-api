package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.AppRoleDTO;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.exceptions.ExistingUsernameException;
import com.backend.bankingsystem.exceptions.InvalidUserRoleException;

import java.util.List;

public interface AppUserService {
    AppUserDTO getCurrentUser(String username);

    List<AppUserDTO> getAppUsers();

    List<AppRoleDTO> getRoles();

    AppUserDTO createAppUser(String newUsername, String password, String customerId, String roleName) throws ExistingUsernameException, CustomerNotFoundException, InvalidUserRoleException;
}
