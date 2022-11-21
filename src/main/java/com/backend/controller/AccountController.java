package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.repositories.AccountsRepo;
import com.backend.usecases.AccountManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    public static AccountsRepo accountsRepo;

    public AccountController(AccountsRepo accountsRepo) {
        AccountController.accountsRepo = accountsRepo;
    }

    @PostMapping("/login" )
    public ResponseEntity<Object> loginAccount(@RequestParam String username, String password){
        // Run AccountManager service
        return AccountManager.loginAccount(username, password);
    }

    @PostMapping("/logout" )
    public ResponseEntity<Object> logoutAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return AccountManager.logoutAccount(new SessionID(sessionID));
    }

    @PostMapping("/register" )
    public ResponseEntity<Object> registerAccount(@RequestParam String username, String password){
        // Run AccountManager service
        return AccountManager.registerAccount(username, password);
    }

    @DeleteMapping("/delete" )
    public ResponseEntity<Object> deleteAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return AccountManager.deleteAccount(new SessionID(sessionID));
    }

    @GetMapping("/account" )
    public ResponseEntity<Object> getAccount(@RequestParam String sessionID) {
        // Run AccountManager service
        return AccountManager.getAccountInfo(new SessionID(sessionID));
    }
}
