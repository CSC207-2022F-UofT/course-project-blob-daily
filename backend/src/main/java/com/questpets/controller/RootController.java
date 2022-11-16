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

}
