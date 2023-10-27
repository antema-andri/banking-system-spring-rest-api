package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.CustomerDTO;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CustomerController {
    private final BankAccountService bankAccountService;

    @GetMapping("customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId){
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO addNewCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId){
        bankAccountService.deleteCustomer(customerId);
    }

    @GetMapping("customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> customerList(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("customers/search")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="world", defaultValue = "") String world){
        return bankAccountService.searchCustomers(world);
    }
}
