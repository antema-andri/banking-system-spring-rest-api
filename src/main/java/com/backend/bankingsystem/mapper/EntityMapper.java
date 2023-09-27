package com.backend.bankingsystem.mapper;

import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CurrentAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.dto.SavingAccountDTO;
import com.backend.bankingsystem.model.BankAccount;
import com.backend.bankingsystem.model.CurrentAccount;
import com.backend.bankingsystem.model.Customer;
import com.backend.bankingsystem.model.SavingAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    /*Customer DTO*/
    CustomerDTO fromEntity(Customer customer);
    Customer fromDTO(CustomerDTO customerDTO);

    /*CurrentAccount DTO*/
    CurrentAccountDTO fromEntity(CurrentAccount currentAccount);
    CurrentAccount fromDTO(CurrentAccountDTO currentAccountDTO);

    /*BankAccount DTO*/
    SavingAccountDTO fromEntity(SavingAccount savingAccount);
    SavingAccount fromDTO(SavingAccountDTO savingAccountDTO);
}
