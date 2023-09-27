package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AccountOperationRepository;
import com.backend.bankingsystem.dao.BankAccountRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CurrentAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.dto.SavingAccountDTO;
import com.backend.bankingsystem.enums.OperationType;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final EntityMapper entityMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer=customerRepository.save(entityMapper.fromDTO(customerDTO));
        return entityMapper.fromEntity(customer);
    }

    @Override
    public CurrentAccountDTO createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        CustomerDTO customerDTO=entityMapper.fromEntity(customer);
        CurrentAccountDTO currentAccountDTO=new CurrentAccountDTO();
        currentAccountDTO.setId(UUID.randomUUID().toString());
        currentAccountDTO.setCreatedAt(new Date());
        currentAccountDTO.setBalance(balance);
        currentAccountDTO.setCustomerDTO(customerDTO);
        currentAccountDTO.setOverDraft(overDraft);
        CurrentAccount currentAccount=bankAccountRepository.save(entityMapper.fromDTO(currentAccountDTO));
        return entityMapper.fromEntity(currentAccount);
    }

    @Override
    public SavingAccountDTO createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        CustomerDTO customerDTO=entityMapper.fromEntity(customer);
        SavingAccountDTO savingAccountDTO=new SavingAccountDTO();
        savingAccountDTO.setId(UUID.randomUUID().toString());
        savingAccountDTO.setCreatedAt(new Date());
        savingAccountDTO.setBalance(balance);
        savingAccountDTO.setCustomerDTO(customerDTO);
        savingAccountDTO.setInterestRate(interestRate);
        SavingAccount savingAccount=bankAccountRepository.save(entityMapper.fromDTO(savingAccountDTO));
        return entityMapper.fromEntity(savingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();
        return customers.stream().map(customer->entityMapper.fromEntity(customer)).collect(Collectors.toList());
    }

    BankAccount findBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
        if(bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return  entityMapper.fromEntity(currentAccount);
        }else {
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return entityMapper.fromEntity(savingAccount);
        }
    }

    @Override
    public void debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
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
        BankAccount bankAccount=findBankAccount(bankAccountId);
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
    public List<BankAccountDTO> listBankAccount(){
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        return bankAccounts.stream().map(ba->{
            if(ba instanceof CurrentAccount) {
                return entityMapper.fromEntity((CurrentAccount) ba);
            }else{
                return entityMapper.fromEntity((SavingAccount) ba);
            }
        }).collect(Collectors.toList());
    }
}
