package com.backend.bankingsystem.service;

public interface AppService{
    void loadData();
    void loadCustomers();

    void loadAccounts();

    void operations();

    void loadAppUsers();
    void loadRoles();
}
