package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppRoleRepository;
import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.AppRoleDTO;
import com.backend.bankingsystem.dto.AppUserDTO;
import com.backend.bankingsystem.enums.UserRole;
import com.backend.bankingsystem.exceptions.EntityNotFoundException;
import com.backend.bankingsystem.exceptions.ExistingUsernameException;
import com.backend.bankingsystem.exceptions.InvalidUserRoleException;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.AppRole;
import com.backend.bankingsystem.model.AppUser;
import com.backend.bankingsystem.model.Customer;
import com.backend.bankingsystem.utils.UtilFileReader;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public void loadAppUsers(){
        try {
            Customer customer=customerRepository.findById(1L)
                    .orElseThrow(()->new EntityNotFoundException("Cutomer not found"));
            AppRole roleUser=appRoleRepository.findById(2L)
                    .orElseThrow(()->new EntityNotFoundException("Role not found"));
            AppRole roleAdmin=appRoleRepository.findById(1L)
                    .orElseThrow(()->new EntityNotFoundException("Role not found"));
            List<AppUserDTO> appUserDTOs= UtilFileReader.readJsonArray("jsondata/users_test.json",AppUserDTO.class);
            int count=0;
            int userNumber=2;

            for (AppUserDTO userDTO:appUserDTOs){
                if(count==0) {
                    userDTO.setAppRole(entityMapper.fromEntity(roleAdmin));
                }else{
                    userDTO.setCustomer(entityMapper.fromEntity(customer));
                    userDTO.setAppRole(entityMapper.fromEntity(roleUser));
                }
                this.createAppUser(userDTO);
                count++;
                if(count==userNumber) break;
            }
        } catch (IOException | ExistingUsernameException | EntityNotFoundException | InvalidUserRoleException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppUserDTO getCurrentUser(String username) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppUserDTO appUserDTO=entityMapper.fromEntity(appUser);
        return appUserDTO;
    }

    @Override
    public List<AppUserDTO> getAppUsers(){
        List<AppUser> appUsers=appUserRepository.findAll();
        return appUsers.stream().map(entityMapper::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<AppRoleDTO> getRoles() {
        List<AppRole> roles=appRoleRepository.findAll();
        return roles.stream().map(entityMapper::fromEntity).collect(Collectors.toList());
    }

    @Override
    public AppUserDTO createAppUser(AppUserDTO appUserDTO) throws ExistingUsernameException, EntityNotFoundException, InvalidUserRoleException{
        AppUser appUser;
        AppRole appRole;
        AppUser existingAppUser;
        Customer customerUser;
        String encodedPassword;

        existingAppUser=appUserRepository.findByUsername(appUserDTO.getUsername());
        if(existingAppUser!=null){
            throw new ExistingUsernameException("username: '"+appUserDTO.getUsername()+"' already exist");
        }

        appRole=appRoleRepository.findByRoleName(appUserDTO.getAppRole().getRoleName())
                .orElseThrow(()->new EntityNotFoundException("Role with name:"+appUserDTO.getAppRole().getRoleName()+" not found"));

        if(appRole.getRoleName().compareTo(UserRole.USER.name())==0){
            customerUser=customerRepository.findById(appUserDTO.getCustomer().getId())
                    .orElseThrow(()->new EntityNotFoundException("No customer found for the new user"));
            appUserDTO.setCustomer(entityMapper.fromEntity(customerUser));
        }

        if(appRole.getRoleName().compareTo(UserRole.ADMIN.name())==0){
            appUserDTO.setCustomer(null);
        }

        encodedPassword=new BCryptPasswordEncoder().encode(appUserDTO.getPassword());
        appUser=entityMapper.fromDTO(appUserDTO);
        appUser.setPassword(encodedPassword);
        appUser.setAppRole(appRole);
        return entityMapper.fromEntity(appUserRepository.save(appUser));
    }
}
