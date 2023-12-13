package com.backend.bankingsystem.dao;

import com.backend.bankingsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNameContainingIgnoreCase(String world);
    List<Customer> findByAppUserIsNull();
}
