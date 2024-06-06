package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppRoleRepository;
import com.backend.bankingsystem.enums.UserRole;
import com.backend.bankingsystem.model.AppRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppRoleServiceImpl implements AppRoleService{
    private final AppRoleRepository appRoleRepository;

    @Override
    public void loadAppRoles() {
        for (UserRole userRole : UserRole.values()) {
            AppRole appRole=new AppRole();
            appRole.setRoleName(userRole.toString());
            appRoleRepository.save(appRole);
        }
    }
}
