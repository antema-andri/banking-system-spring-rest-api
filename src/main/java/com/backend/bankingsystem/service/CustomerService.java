package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    void loadCustomers();

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomer(Long customerId);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long idCustomer);

    List<CustomerDTO> listCustomers();

    List<CustomerDTO> findCustomers();

    List<CustomerDTO> searchCustomers(String word);
}
