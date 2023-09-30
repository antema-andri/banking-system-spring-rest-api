package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.exceptions.CustomerNotFoundException;
import com.backend.bankingsystem.utils.UtilFileReader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService{
    public final BankAccountService bankAccountService;

    @Override
    public void loadData() {
        /* create customers */
        List<CustomerDTO> customerDTOList;
        try {
            customerDTOList=UtilFileReader.readJsonArray("static/jsondata/customers.json",CustomerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        customerDTOList.forEach(customerDTO->bankAccountService.saveCustomer(customerDTO));

        /* create accounts */
        List<CustomerDTO> customerList=bankAccountService.listCustomers();
        for (CustomerDTO customerDTO:customerList){
            try {
                bankAccountService.createCurrentAccount(Math.random()*250000, customerDTO.getId(), Math.random()*500000);
                bankAccountService.createSavingAccount(Math.random()*355000, customerDTO.getId(), Math.random()*500000);
            } catch (CustomerNotFoundException e) {
                e.printStackTrace();
            }
        }

        /* create operations */
        List<BankAccountDTO> bankAccountList=bankAccountService.listBankAccount();
        for(BankAccountDTO bankAccountDTO:bankAccountList){
            for(int i=0; i<10; i++){
                try {
                    double creditAmount=1000+Math.random()*900000;
                    double debitAmount=500+Math.random()*100000;
                    bankAccountService.credit(bankAccountDTO.getId(), creditAmount, "Credit "+creditAmount);
                    bankAccountService.debit(bankAccountDTO.getId(), debitAmount, "Debit "+debitAmount);
                } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
