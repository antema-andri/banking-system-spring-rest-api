package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.exceptions.*;
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
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") String accountId) throws EntityNotFoundException {
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
    ) throws EntityNotFoundException {
        return bankAccountService.accountHistoryPage(accountId, page, size);
    }

    @PostMapping("accounts/credit")
    public BankAccountDTO credit(@RequestBody  CreditDTO creditDTO) throws EntityNotFoundException {
        return bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDesc());
    }

    @PostMapping("accounts/debit")
    public BankAccountDTO debit(@RequestBody DebitDTO debitDTO) throws EntityNotFoundException, BalanceNotSufficientException, BadAmountException {
        return bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDesc());
    }

    @PostMapping("accounts/transfer")
    public BankAccountDTO transfer(@RequestBody TransferDTO transferDTO) throws EntityNotFoundException, BalanceNotSufficientException, BadAmountException, SameAccountException {
        return bankAccountService.localTransfer(transferDTO.getAccountSourceId(), transferDTO.getAccountDestinationId(), transferDTO.getAmount());
    }

    @GetMapping("accounts/page")
    public BankAccountPageDTO getBankAccountPage(
            @RequestParam(name = "name") String customerName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        return bankAccountService.getBankAccountPage(customerName,page,size);
    }

    @GetMapping("accounts/customer/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> customerBankAccounts(
        @PathVariable(name = "customerId") String customerId
    ) throws EntityNotFoundException {
        return bankAccountService.getCustomerAccounts(customerId);
    }
}
