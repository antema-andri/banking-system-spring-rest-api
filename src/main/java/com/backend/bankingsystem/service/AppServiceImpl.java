package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AppRoleRepository;
import com.backend.bankingsystem.dao.AppUserRepository;
import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.enums.UserRole;
import com.backend.bankingsystem.exceptions.BadAmountException;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.EntityNotFoundException;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.AppRole;
import com.backend.bankingsystem.model.AppUser;
import com.backend.bankingsystem.model.Customer;
import com.backend.bankingsystem.utils.UtilFileReader;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService{
    private final BankAccountService bankAccountService;
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final EntityMapper entityMapper;


    @Override
    public void loadData() {
        /* create customers */
        List<CustomerDTO> customerDTOList;
        try {
            customerDTOList=UtilFileReader.readJsonArray("jsondata/customers.json",CustomerDTO.class);
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
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }

        /* create operations */
        List<BankAccountDTO> bankAccountList=bankAccountService.listBankAccount();
        for(BankAccountDTO bankAccountDTO:bankAccountList){
            for(int i=0; i<10; i++){
                try {
                    double creditAmount=10000+Math.random()*900000;
                    double debitAmount=10+Math.random()*100000;
                    bankAccountService.credit(bankAccountDTO.getId(), creditAmount, "Credit "+creditAmount);
                    bankAccountService.debit(bankAccountDTO.getId(), debitAmount, "Debit "+debitAmount);
                } catch (EntityNotFoundException | BalanceNotSufficientException | BadAmountException e) {
                    e.printStackTrace();
                }
            }
        }

        /* create Users */
        this.loadAppUsers();
    }

    public void loadAppUsers(){
        Customer customer=entityMapper.fromDTO(bankAccountService.getCustomer(1L));
        this.loadRoles();
        List<AppUser> appUsers;
        int userNumber=2;
        try {
            appUsers=UtilFileReader.readJsonArray("jsondata/users_test.json",AppUser.class);
            int count=0;
            for (AppUser user:appUsers){
                BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                if(count==0) {
                    user.setAppRole(appRoleRepository.findById(1L).orElseThrow());
                }else{
                    user.setCustomer(customer);
                    user.setAppRole(appRoleRepository.findById(2L).orElseThrow());
                }
                appUserRepository.save(user);
                count++;
                if(count==userNumber) break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadRoles() {
        for (UserRole userRole : UserRole.values()) {
            AppRole appRole=new AppRole();
            appRole.setRoleName(userRole.toString());
            appRoleRepository.save(appRole);
        }
    }
}
