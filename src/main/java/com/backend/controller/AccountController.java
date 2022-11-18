package com.backend.controller;

import com.backend.repositories.AccountsRepo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    public static AccountsRepo accountsRepo;

    public AccountController(AccountsRepo accountsRepo) {
        AccountController.accountsRepo = accountsRepo;
    }
}