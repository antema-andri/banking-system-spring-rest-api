package com.backend.bankingsystem.web;

import com.backend.bankingsystem.model.Customer;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerController {
    private final BankAccountService bankAccountService;

    @GetMapping("customers")
    public List<Customer> customers(){
        return bankAccountService.listCustomers();
    }
}
