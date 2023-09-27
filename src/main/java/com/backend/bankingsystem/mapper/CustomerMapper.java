package com.backend.bankingsystem.mapper;

import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO fromEntity(Customer customer);
    Customer fromDTO(CustomerDTO customerDTO);
}
