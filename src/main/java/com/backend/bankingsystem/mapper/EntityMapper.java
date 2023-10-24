package com.backend.bankingsystem.mapper;

import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    /*Customer DTO*/
    @Mapping(target = "bankAccounts", ignore = true)
    CustomerDTO fromEntity(Customer customer);
    Customer fromDTO(CustomerDTO customerDTO);
    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customer);

    /*BankAccount DTO*/
    @SubclassMapping(source = CurrentAccount.class, target = CurrentAccountDTO.class)
    @SubclassMapping(source = SavingAccount.class, target = SavingAccountDTO.class)
    @Mapping(target = "type", qualifiedByName = "getValueType", source = ".")
    BankAccountDTO fromEntity(BankAccount bankAccount);
    @Named("getValueType")
    default String valueOfAttributeType(BankAccount bankAccount) {
        return bankAccount.getClass().getSimpleName();
    }
    @SubclassMapping(source = CurrentAccountDTO.class, target = CurrentAccount.class)
    @SubclassMapping(source = SavingAccountDTO.class, target = SavingAccount.class)
    BankAccount fromDTO(BankAccountDTO bankAccountDTO);
    List<BankAccountDTO> bankAccountsToBankAccountDTOs(List<BankAccount> bankAccounts);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "customer", target = "customer")
    AppUserDTO fromEntity(AppUser appUser);
    AppUser fromDTO(AppUserDTO appUserDTO);
}
