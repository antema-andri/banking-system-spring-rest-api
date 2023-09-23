package com.backend.bankingsystem.dao;

import com.backend.bankingsystem.model.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
