package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.usecases.managers.AccountManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {
    /**
     * Post request to log in with the given credentials
     * @param username of type String, username to reference associated account
     * @param password of type String, password to validate associated account
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    @PostMapping("/login" )
    public ResponseEntity<Object> loginAccount(@RequestParam String username, String password){
        // Run AccountManager service
        return AccountManager.loginAccount(username, password);
    }

    /**
     * Post request to log out with the given credentials
     * @param sessionID of type String, sessionID to reference associated account
     * @return a response entity detailing successful completion or any associated error
     */
    @PostMapping("/logout" )
    public ResponseEntity<Object> logoutAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return AccountManager.logoutAccount(new SessionID(sessionID));
    }

    /**
     * Post request to register a new account with the given credentials
     * @param username of type String, username for account creation (account credential)
     * @param password of type String, password for account creation (account credential)
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    @PostMapping("/register" )
    public ResponseEntity<Object> registerAccount(@RequestParam String username, String password){
        // Run AccountManager service
        return AccountManager.registerAccount(username, password);
    }

    /**
     * Delete request to delete the account associated with the given parameter
     * @param sessionID of type String, sessionID to reference the account to be deleted
     * @return a response entity detailing successful completion or any associated error
     */
    @DeleteMapping("/delete" )
    public ResponseEntity<Object> deleteAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return AccountManager.deleteAccount(new SessionID(sessionID));
    }

    /**
     * Get request to retrieve the information associated with the given parameter
     * @param sessionID of type String, sessionID to reference the associated account
     * @return a response entity detailing successful completion (with the information of the associated Protected Account entity) or any associated error
     */
    @GetMapping("/account" )
    public ResponseEntity<Object> getAccount(@RequestParam String sessionID) {
        // Run AccountManager service
        return AccountManager.getAccountInfo(new SessionID(sessionID));
    }
}
