package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.exceptions.BalanceNotSufficientException;
import com.backend.bankingsystem.exceptions.BankAccountNotFoundException;
import com.backend.bankingsystem.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping("accounts")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountService.listBankAccount();
    }

    @GetMapping("accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") String accountId) throws BankAccountNotFoundException {
       return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("accounts/{id}/operations")
    public List<AccountOperationDTO> getOperations(@PathVariable(name = "id") String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("accounts/{id}/operationsPage")
    public AccountHistoryDTO getOperationPages(
            @PathVariable(name = "id") String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) throws BankAccountNotFoundException {
        return bankAccountService.accountHistoryPage(accountId, page, size);
    }

    @PostMapping("accounts/credit")
    public void credit(@RequestBody  CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDesc());
    }

    @PostMapping("accounts/debit")
    public void debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDesc());
    }

    @PostMapping("accounts/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.localTransfer(transferDTO.getAccountSourceId(), transferDTO.getAccountDestinationId(), transferDTO.getAmount());
    }
}
