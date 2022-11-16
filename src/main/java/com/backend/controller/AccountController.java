package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.AccountsRepo;
import com.backend.usecases.AccountManager;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    public static AccountsRepo accountsRepo;

    public AccountController(AccountsRepo accountsRepo) {
        AccountController.accountsRepo = accountsRepo;
    }

    @PostMapping("/login" )
    public String loginAccount(@RequestParam String username, String password){
        return String.format("%s and %s for login", username, password);
    }

    @PostMapping("/logout" )
    public String logoutAccount(@RequestParam String sessionID){

        return "logout";
    }

    @PostMapping("/register" )
    public String registerAccount(@RequestParam String username, String password){
        SessionID sessionID = AccountManager.registerAccount(username, password);
        if (sessionID == null) return "Invalid username or password";
        return sessionID.toString();
    }

    @DeleteMapping("/delete" )
    public String deleteAccount(@RequestParam String sessionID){
        return "delete";
    }

    @GetMapping("/account" )
    public ProtectedAccount getAccount(@RequestParam String sessionID){
        return AccountManager.getAccountInfo(new SessionID(sessionID));
    }
}
