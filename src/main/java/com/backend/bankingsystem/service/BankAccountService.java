package com.backend.bankingsystem.service;

import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.model.BankAccount;
import com.backend.bankingsystem.model.Customer;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    BankAccount createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException;
    BankAccount createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    void debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException;
    void localTransfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccount> listBankAccount();
}
