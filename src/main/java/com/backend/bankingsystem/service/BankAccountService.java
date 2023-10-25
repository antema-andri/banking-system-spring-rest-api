package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CurrentAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.dto.SavingAccountDTO;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(Long customerId);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long idCustomer);
    CurrentAccountDTO createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException;
    SavingAccountDTO createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    void debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException;
    void localTransfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDTO> listBankAccount();
}
