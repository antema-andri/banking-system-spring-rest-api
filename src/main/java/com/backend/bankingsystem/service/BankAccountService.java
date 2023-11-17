package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.exceptions.BadAmountException;
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

    BankAccountDTO debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException, BadAmountException;

    BankAccountDTO credit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException;
    void localTransfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException, BadAmountException;
    List<BankAccountDTO> listBankAccount();

    List<CustomerDTO> searchCustomers(String world);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO accountHistoryPage(String accountId, int page, int size) throws BankAccountNotFoundException;

    BankAccountPageDTO getBankAccountPage(String customerName, int page, int size);
}
