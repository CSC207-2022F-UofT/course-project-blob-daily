package com.questpets.controller;

import com.questpets.entities.users.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @Autowired
    private com.questpets.repositories.AccountsRepo accountsRepo;

    @GetMapping("/")
    public String root(){
        accountsRepo.save(new Account(
                new com.questpets.entities.IDs.AccountID(null),
                "Shaan03",
                "CurryMuncher27;",
                new java.sql.Timestamp(System.currentTimeMillis())
        ));
        return "Hello World!";
    }
}
