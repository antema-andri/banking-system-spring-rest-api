package com.backend.bankingsystem.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/health")
    public String test(){
        return "data test";
    }

}
