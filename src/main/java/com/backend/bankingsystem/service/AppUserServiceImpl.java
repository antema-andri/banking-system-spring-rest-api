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
        Customer customer=customerRepository.findById(1L).orElseThrow(()->new jakarta.persistence.EntityNotFoundException());
        AppRole roleUser=appRoleRepository.findById(2L).orElseThrow();
        AppRole roleAdmin=appRoleRepository.findById(1L).orElseThrow();
        List<AppUserDTO> appUserDTOs;
        int userNumber=2;
        try {
            appUserDTOs= UtilFileReader.readJsonArray("jsondata/users_test.json",AppUserDTO.class);
            int count=0;
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
        return appUsers.stream().map(appUser->entityMapper.fromEntity(appUser)).collect(Collectors.toList());
    }

    @Override
    public List<AppRoleDTO> getRoles() {
        List<AppRole> roles=appRoleRepository.findAll();
        return roles.stream().map(r->entityMapper.fromEntity(r)).collect(Collectors.toList());
    }

    @Override
    public AppUserDTO createAppUser(String newUsername, String password, String customerId, String roleName) throws ExistingUsernameException, EntityNotFoundException, InvalidUserRoleException {
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
                throw new EntityNotFoundException("No customer found for the new user");
            }
        }

        appUser=new AppUser();
        appUser.setUsername(newUsername);
        appUser.setPassword(new BCryptPasswordEncoder().encode(password));
        appUser.setAppRole(appRole);
        appUser.setCustomer(customerUser);
        return entityMapper.fromEntity(appUserRepository.save(appUser));
    }

    @Override
    public AppUserDTO createAppUser(AppUserDTO appUserDTO) throws ExistingUsernameException, EntityNotFoundException, InvalidUserRoleException{
        AppUser appUser;
        AppRole appRole=appRoleRepository.findByRoleName(appUserDTO.getAppRole().getRoleName());
        AppUser existingAppUser=appUserRepository.findByUsername(appUserDTO.getUsername());
        Customer customerUser;
        String encodedPassword;

        if(existingAppUser!=null){
            throw new ExistingUsernameException("username: '"+appUserDTO.getUsername()+"' already exist");
        }

        if(appRole.getRoleName().compareTo(UserRole.USER.name())==0){
            customerUser=customerRepository.findById(appUserDTO.getCustomer().getId()).orElse(null);
            if(customerUser==null){
                throw new EntityNotFoundException("No customer found for the new user");
            }
        }

        encodedPassword=new BCryptPasswordEncoder().encode(appUserDTO.getPassword());
        appUserDTO.setPassword(encodedPassword);
        appUser=entityMapper.fromDTO(appUserDTO);
        return entityMapper.fromEntity(appUserRepository.save(appUser));
    }
}
