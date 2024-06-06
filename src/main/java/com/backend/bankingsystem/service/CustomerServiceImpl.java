package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.Customer;
import com.backend.bankingsystem.utils.UtilFileReader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final EntityMapper entityMapper;
    private final CustomerRepository customerRepository;

    @Override
    public void loadCustomers() {
        List<CustomerDTO> customerDTOList;
        try {
            customerDTOList= UtilFileReader.readJsonArray("jsondata/customers.json",CustomerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        customerDTOList.forEach(customerDTO -> this.saveCustomer(customerDTO));
    }

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

    @Override
    public List<CustomerDTO> findCustomers() {
        List<Customer> customers=customerRepository.findByAppUserIsNull();
        System.out.println(customers);
        return customers.stream().map(entityMapper::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String word) {
        List<Customer> customers=customerRepository.findByNameContainingIgnoreCase(word);
        return customers.stream().map(entityMapper::fromEntity).collect(Collectors.toList());
    }

}
