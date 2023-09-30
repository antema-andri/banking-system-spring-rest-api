package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerController {
    private final BankAccountService bankAccountService;
    @GetMapping("customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId){
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("customers")
    public CustomerDTO addNewCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @GetMapping("customers")
    public List<CustomerDTO> customerList(){
        return bankAccountService.listCustomers();
    }
}
