package com.backend.bankingsystem.web;

import com.backend.bankingsystem.dao.CustomerRepository;
import com.backend.bankingsystem.mapper.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {
    private final CustomerRepository customerRepository;
    private final EntityMapper entityMapper;

    @GetMapping("/health")
    public String test(){
        return "data test";
    }

}
