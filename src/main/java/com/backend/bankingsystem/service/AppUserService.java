package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.AppRoleDTO;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.exceptions.EntityNotFoundException;
import com.backend.bankingsystem.exceptions.ExistingUsernameException;
import com.backend.bankingsystem.exceptions.InvalidUserRoleException;

import java.util.List;

public interface AppUserService {
    void loadAppUsers();

    AppUserDTO getCurrentUser(String username);

    List<AppUserDTO> getAppUsers();

    List<AppRoleDTO> getRoles();

    AppUserDTO createAppUser(AppUserDTO appUserDTO) throws ExistingUsernameException, EntityNotFoundException, InvalidUserRoleException;
}
