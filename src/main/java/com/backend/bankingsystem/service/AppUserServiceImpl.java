package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppRoleRepository;
import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.AppRoleDTO;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.enums.UserRole;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.exceptions.ExistingUsernameException;
import com.backend.bankingsystem.exceptions.InvalidUserRoleException;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.AppRole;
import com.backend.bankingsystem.model.AppUser;
import com.backend.bankingsystem.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final AppRoleRepository appRoleRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AppUserDTO getCurrentUser(String username) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppUserDTO appUserDTO=entityMapper.fromEntity(appUser);
        return appUserDTO;
    }

    @Override
    public List<AppUserDTO> getAppUsers(){
        List<AppUser> appUsers=appUserRepository.findAll();
        return appUsers.stream().map(appUser->entityMapper.fromEntity(appUser)).collect(Collectors.toList());
    }

    @Override
    public List<AppRoleDTO> getRoles() {
        List<AppRole> roles=appRoleRepository.findAll();
        return roles.stream().map(r->entityMapper.fromEntity(r)).collect(Collectors.toList());
    }

    @Override
    public AppUserDTO createAppUser(String newUsername, String password, String customerId, String roleName) throws ExistingUsernameException, CustomerNotFoundException, InvalidUserRoleException {
        AppUser appUser;
        AppRole appRole=appRoleRepository.findByRoleName(roleName);
        AppUser existingAppUser=appUserRepository.findByUsername(newUsername);
        Customer customerUser=null;

        if(existingAppUser!=null){
            throw new ExistingUsernameException("username: '"+newUsername+"' already exist");
        }

        if(appRole.getRoleName().compareTo(UserRole.USER.name())==0){
            customerUser=customerRepository.findById(Long.parseLong(customerId)).orElse(null);
            if(customerUser==null){
                throw new CustomerNotFoundException("No customer found for the new user");
            }
        }

        appUser=new AppUser();
        appUser.setUsername(newUsername);
        appUser.setPassword(new BCryptPasswordEncoder().encode(password));
        appUser.setAppRole(appRole);
        appUser.setCustomer(customerUser);
        return entityMapper.fromEntity(appUserRepository.save(appUser));
    }
}
