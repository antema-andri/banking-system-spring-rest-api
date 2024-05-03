package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.exceptions.*;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(Long customerId);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long idCustomer);
    CurrentAccountDTO createCurrentAccount(double balance, Long customerId, double overDraft) throws EntityNotFoundException;
    SavingAccountDTO createSavingAccount(double balance, Long customerId, double interestRate) throws EntityNotFoundException;
    List<CustomerDTO> listCustomers();

    List<CustomerDTO> findCustomers();

    BankAccountDTO getBankAccount(String bankAccountId) throws EntityNotFoundException;

    BankAccountDTO debit(String bankAccountId, double amount, String desc) throws EntityNotFoundException, BalanceNotSufficientException, BadAmountException;

    BankAccountDTO credit(String bankAccountId, double amount, String desc) throws EntityNotFoundException;
    BankAccountDTO localTransfer(String accountSourceId, String accountDestinationId, double amount) throws EntityNotFoundException, BalanceNotSufficientException, BadAmountException, SameAccountException;
    List<BankAccountDTO> listBankAccount();

    List<CustomerDTO> searchCustomers(String world);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO accountHistoryPage(String accountId, int page, int size) throws EntityNotFoundException;

    BankAccountPageDTO getBankAccountPage(String customerName, int page, int size);

    List<BankAccountDTO> getCustomerAccounts(String customerId) throws EntityNotFoundException;
}
