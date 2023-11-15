package com.backend.bankingsystem.dao;

import com.backend.bankingsystem.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Page<BankAccount> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
}
