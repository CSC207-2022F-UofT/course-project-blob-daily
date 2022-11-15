package com.questpets.controller;

import com.questpets.entities.users.Account;
import com.questpets.entities.users.DBAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @Autowired
    private com.questpets.repositories.AccountsRepo accountsRepo;

    @GetMapping("/" )
    public String root(){
        Account acc = new Account(
                new com.questpets.entities.IDs.AccountID("298374j"),
                "Shaan03",
                "CurryMuncher27;",
                new java.sql.Timestamp(System.currentTimeMillis())
        );
        DBAccount dbAcc = new DBAccount(acc);
        accountsRepo.save(dbAcc);
        return "Hello World!";
    }
}
