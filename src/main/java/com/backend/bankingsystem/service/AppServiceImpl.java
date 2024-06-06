package com.backend.bankingsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService{
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final AppRoleService appRoleService;
    private final AppUserService appUserService;

    @Override
    public void loadData() {
        /* create customers */
        this.loadCustomers();

        /* create accounts */
        this.loadAccounts();

        /* create operations */
        this.operations();

        /* crate Roles */
        this.loadRoles();

        /* create Users */
        this.loadAppUsers();
    }

    @Override
    public void loadCustomers(){
        customerService.loadCustomers();
    }

    @Override
    public void loadAccounts(){
        bankAccountService.loadAccounts();
    }

    @Override
    public void operations(){
        bankAccountService.loadOperations();
    }

    @Override
    public void loadAppUsers(){
        appUserService.loadAppUsers();
    }

    @Override
    public void loadRoles() {
        appRoleService.loadAppRoles();
    }
}
