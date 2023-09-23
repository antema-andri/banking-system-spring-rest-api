package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AccountOperationRepository;
import com.backend.bankingsystem.dao.BankAccountRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.enums.AccountStatus;
import com.backend.bankingsystem.enums.OperationType;
import com.backend.bankingsystem.model.AccountOperation;
import com.backend.bankingsystem.model.CurrentAccount;
import com.backend.bankingsystem.model.SavingAccount;
import com.backend.bankingsystem.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class AppService {
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;

    public void loadData(){
        Stream.of("custom1", "custom2", "custom3").forEach(name->{
            Customer customer=new Customer();
            customer.setName(name);
            customer.setEmail(name+"@gmail.com");
            customerRepository.save(customer);
        });
        customerRepository.findAll().forEach(customer -> {
            CurrentAccount currentAccount=new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setBalance(Math.random()*90000);
            currentAccount.setCreatedAt(new Date());
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setCustomer(customer);
            currentAccount.setOverDraft(9000);
            bankAccountRepository.save(currentAccount);

            SavingAccount savingAccount=new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setBalance(Math.random()*90000);
            savingAccount.setCreatedAt(new Date());
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setCustomer(customer);
            savingAccount.setInterestRate(2.0);
            bankAccountRepository.save(savingAccount);
        });
        bankAccountRepository.findAll().forEach(ba->{
            for(int i=0;i<5;i++){
                AccountOperation accountOperation=new AccountOperation();
                accountOperation.setAmount(Math.random()*10000);
                accountOperation.setDate(new Date());
                accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                accountOperation.setBankAccount(ba);
                accountOperationRepository.save(accountOperation);
            }
        });
    }
}
