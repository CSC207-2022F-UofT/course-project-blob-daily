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
        SessionID sessionID = AccountManager.loginAccount(username, password);

        // Check for errors
        if (sessionID == null) return new ResponseEntity<Object>("Something went wrong", HttpStatus.NOT_FOUND);

        // Response
        return new ResponseEntity<Object>(sessionID.getID(), HttpStatus.OK);
    }

    @PostMapping("/logout" )
    public ResponseEntity<Object> logoutAccount(@RequestParam String sessionID){
        // Run AccountManager service
        boolean isSuccessful = AccountManager.logoutAccount(new SessionID(sessionID));

        // Check for errors
        if (!isSuccessful) return new ResponseEntity<Object>("Something went wrong", HttpStatus.NOT_FOUND);

        // Response
        return new ResponseEntity<Object>("Successfully Logged out", HttpStatus.OK);
    }

    @PostMapping("/register" )
    public ResponseEntity<Object> registerAccount(@RequestParam String username, String password){
        // Run AccountManager service
        SessionID sessionID = AccountManager.registerAccount(username, password);

        // Check for errors
        if (sessionID == null) return new ResponseEntity<Object>("Something went wrong", HttpStatus.NOT_FOUND);

        // Response
        return new ResponseEntity<Object>(sessionID.getID(), HttpStatus.OK);
    }

    @DeleteMapping("/delete" )
    public ResponseEntity<Object> deleteAccount(@RequestParam String sessionID){
        // Run AccountManager service
        boolean isSuccessful = AccountManager.deleteAccount(new SessionID(sessionID));

        // Check for errors
        if (!isSuccessful) return new ResponseEntity<Object>("Something went wrong", HttpStatus.NOT_FOUND);

        // Response
        return new ResponseEntity<Object>("Successfully Deleted Account!", HttpStatus.OK);
    }

    @GetMapping("/account" )
    public ResponseEntity<Object> getAccount(@RequestParam String sessionID) {
        // Run AccountManager service
        ProtectedAccount protectedAccount = AccountManager.getAccountInfo(new SessionID(sessionID));

        // Check for errors
        if (protectedAccount == null) return new ResponseEntity<Object>("Something went wrong", HttpStatus.NOT_FOUND);

        // Response
        return new ResponseEntity<Object>(protectedAccount, HttpStatus.OK);
    }
}
