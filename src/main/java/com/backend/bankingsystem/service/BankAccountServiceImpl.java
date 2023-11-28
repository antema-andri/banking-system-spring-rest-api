package com.backend.bankingsystem.service;

import com.backend.bankingsystem.dao.AccountOperationRepository;
import com.backend.bankingsystem.dao.BankAccountRepository;
import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.dto.*;
import com.backend.bankingsystem.enums.AccountStatus;
import com.backend.bankingsystem.enums.Currency;
import com.backend.bankingsystem.enums.OperationType;
import com.backend.bankingsystem.exceptions.*;
import com.backend.bankingsystem.mapper.EntityMapper;
import com.backend.bankingsystem.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final EntityMapper entityMapper;

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
    public CurrentAccountDTO createCurrentAccount(double balance, Long customerId, double overDraft) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(balance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency(Currency.DOLLAR.toString());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccountDTO currentAccountDTO=(CurrentAccountDTO) entityMapper.fromEntity(bankAccountRepository.save(currentAccount));
        return currentAccountDTO;
    }

    @Override
    public SavingAccountDTO createSavingAccount(double balance, Long customerId, double interestRate) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(balance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCurrency(Currency.DOLLAR.toString());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccountDTO savingAccountDTO=(SavingAccountDTO) entityMapper.fromEntity(bankAccountRepository.save(savingAccount));
        return savingAccountDTO;
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

    BankAccount findBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    }

    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
        return entityMapper.fromEntity(bankAccount);
    }

    @Override
    public BankAccountDTO debit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException, BadAmountException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
        if(amount<=0)
            throw new BadAmountException("Negative or null amount");
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not enough");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setDescription(desc);
        accountOperationRepository.save(accountOperation);
        /* Update balance */
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        return entityMapper.fromEntity(bankAccountRepository.save(bankAccount));
    }

    @Override
    public BankAccountDTO credit(String bankAccountId, double amount, String desc) throws BankAccountNotFoundException {
        BankAccount bankAccount=findBankAccount(bankAccountId);
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setDescription(desc);
        accountOperationRepository.save(accountOperation);
        /* Update balance */
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        return entityMapper.fromEntity(bankAccountRepository.save(bankAccount));
    }

    @Override
    public BankAccountDTO localTransfer(String accountSourceId, String accountDestinationId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException, BadAmountException, SameAccountException {
        if(accountSourceId.equals(accountDestinationId))
            throw new SameAccountException("Same sender and receiver account");
        BankAccountDTO debtorAccount=debit(accountSourceId, amount, "Transfer to "+accountDestinationId);
        credit(accountDestinationId, amount, "Transfer from "+accountSourceId);
        return debtorAccount;
    }

    @Override
    public List<BankAccountDTO> listBankAccount(){
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        return bankAccounts.stream().map(ba->entityMapper.fromEntity(ba)).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String world) {
        List<Customer> customers=customerRepository.findByNameContainingIgnoreCase(world);
        return customers.stream().map(customer->entityMapper.fromEntity(customer)).collect(Collectors.toList());
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(acco->entityMapper.fromEntity(acco)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO accountHistoryPage(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.getContent().stream().map(accop->entityMapper.fromEntity(accop)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperations(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public BankAccountPageDTO getBankAccountPage(String customerName, int page, int size){
        Page<BankAccount> bankAccounts=bankAccountRepository.findByCustomerNameContainingIgnoreCase(customerName, PageRequest.of(page,size));
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.getContent().stream().map(ba->entityMapper.fromEntity(ba)).collect(Collectors.toList());
        BankAccountPageDTO bankAccountPageDTO=new BankAccountPageDTO();
        bankAccountPageDTO.setCurrentPage(page);
        bankAccountPageDTO.setTotalPages(bankAccounts.getTotalPages());
        bankAccountPageDTO.setPageSize(size);
        bankAccountPageDTO.setAccounts(bankAccountDTOS);
        return bankAccountPageDTO;
    }
}
