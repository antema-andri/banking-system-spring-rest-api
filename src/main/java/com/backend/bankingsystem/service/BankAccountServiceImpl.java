package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AccountOperationRepository;
import com.backend.bankingsystem.dao.BankAccountRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CurrentAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.dto.SavingAccountDTO;
import com.backend.bankingsystem.enums.AccountStatus;
import com.backend.bankingsystem.enums.Currency;
import com.backend.bankingsystem.enums.OperationType;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        return entityMapper.fromEntity(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer=entityMapper.fromDTO(customerDTO);
        Customer updatedCustomer=customerRepository.save(customer);
        return entityMapper.fromEntity(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long idCustomer) {
        customerRepository.deleteById(idCustomer);
    }

    @Override
    public CurrentAccountDTO createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(balance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency(Currency.EURO.toString());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccountDTO currentAccountDTO=(CurrentAccountDTO) entityMapper.fromEntity(bankAccountRepository.save(currentAccount));
        return currentAccountDTO;
    }

    @Override
    public SavingAccountDTO createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(balance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCurrency(Currency.EURO.toString());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccountDTO savingAccountDTO=(SavingAccountDTO) entityMapper.fromEntity(bankAccountRepository.save(savingAccount));
        return savingAccountDTO;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();
        List<CustomerDTO> customerDTOs=new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO=entityMapper.fromEntity(customer);
            List<BankAccountDTO> bankAccountDTOList=entityMapper.bankAccountsToBankAccountDTOs(customer.getBankAccounts());
            customerDTO.setBankAccounts(bankAccountDTOList);
            customerDTOs.add(customerDTO);
        }
        return customerDTOs;
    }

    BankAccount findBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
        return entityMapper.fromEntity(bankAccount);
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
        accountOperation.setDescription(desc);
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
        accountOperation.setDescription(desc);
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
        return bankAccounts.stream().map(ba->entityMapper.fromEntity(ba)).collect(Collectors.toList());
    }
}
