package com.backend.bankingsystem.service;

import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.model.BankAccount;
import com.backend.bankingsystem.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService{
    public final BankAccountService bankAccountService;

    @Override
    public void loadData() {
        Stream.of("custom1", "custom2", "custom3").forEach(name->{
            Customer customer=new Customer();
            customer.setName(name);
            customer.setEmail(name+"@gmail.com");
            bankAccountService.saveCustomer(customer);
        });
        /* create accounts */
        List<Customer> customerList=bankAccountService.listCustomers();
        for (Customer customer:customerList){
            try {
                bankAccountService.createCurrentAccount(Math.random()*250000, customer.getId(), Math.random()*500000);
                bankAccountService.createSavingAccount(Math.random()*355000, customer.getId(), Math.random()*500000);
            } catch (CustomerNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        /* create operations */
        List<BankAccount> bankAccountList=bankAccountService.listBankAccount();
        for(BankAccount bankAccount:bankAccountList){
            for(int i=0; i<10; i++){
                try {
                    double creditAmount=1000+Math.random()*900000;
                    double debitAmount=500+Math.random()*100000;
                    bankAccountService.credit(bankAccount.getId(), creditAmount, "Credit "+creditAmount);
                    bankAccountService.debit(bankAccount.getId(), debitAmount, "Debit "+debitAmount);
                } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
