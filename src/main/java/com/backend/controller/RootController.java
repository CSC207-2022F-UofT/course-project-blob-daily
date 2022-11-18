package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @Autowired
    private com.backend.repositories.AccountsRepo accountsRepo;

    @GetMapping("/" )
    public String root(){
//        Account acc = new Account(
//                new com.questpets.entities.IDs.AccountID("null"),
//                "TonyKim02",
//                "CurryMuncher27;",
//                new java.sql.Timestamp(System.currentTimeMillis())
//        );
//        acc.getAccountID().generateID();
//        DBAccount dbAcc = new DBAccount(acc);
//
//        accountsRepo.save(dbAcc);
        return "Hello World!";
    }
}
