package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping("bankAccounts")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountService.listBankAccount();
    }
}
