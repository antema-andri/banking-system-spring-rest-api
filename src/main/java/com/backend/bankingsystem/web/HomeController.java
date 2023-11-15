package com.backend.bankingsystem.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private int counter;

    @GetMapping("/health")
    public String test(){
        counter++;
        return "data test "+counter;
    }
}
