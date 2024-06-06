package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId){
        return customerService.getCustomer(customerId);
    }

    @PostMapping("customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO addNewCustomer(@RequestBody CustomerDTO customerDTO){
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId){
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> customerList(){
        return customerService.listCustomers();
    }

    @GetMapping("customers/search")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="word", defaultValue = "") String word){
        return customerService.searchCustomers(word);
    }

    @GetMapping("customers/notuser")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> getCustomersNotUser(){
        return customerService.findCustomers();
    }
}
