package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.AccountsRepo;
import com.backend.usecases.AccountManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.minidev.json.JSONObject;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
