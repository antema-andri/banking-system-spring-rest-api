package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.BankAccountDTO;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping("bankAccounts")
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountService.listBankAccount();
    }
}
