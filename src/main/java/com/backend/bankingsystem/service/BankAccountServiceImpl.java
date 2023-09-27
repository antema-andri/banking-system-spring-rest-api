package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AccountOperationRepository;
import com.backend.bankingsystem.dao.BankAccountRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.enums.OperationType;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.mapper.CustomerMapper;
import com.backend.bankingsystem.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public BankAccount createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(balance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public BankAccount createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(balance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public void debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=getBankAccount(bankAccountId);
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not enough");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperationRepository.save(accountOperation);
        /* Update balance */
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(bankAccountId);
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperationRepository.save(accountOperation);
        /* Update balance */
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void localTransfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountSourceId, amount, "Transfer to "+accountDestinationId);
        credit(accountDestinationId, amount, "Transfer from "+accountSourceId);
    }

    @Override
    public List<BankAccount> listBankAccount(){
        return bankAccountRepository.findAll();
    }
}
