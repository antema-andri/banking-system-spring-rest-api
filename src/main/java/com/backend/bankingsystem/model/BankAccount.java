package com.backend.bankingsystem.model;

import com.backend.bankingsystem.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    @Column(precision=15, scale=2)
    private BigDecimal balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<AccountOperation> accountOperations;
}
