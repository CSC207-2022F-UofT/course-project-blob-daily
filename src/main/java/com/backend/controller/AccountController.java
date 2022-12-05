package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.AccountSystemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {
    private final AccountSystemFacade accountSystemFacade;

    /**
     * Spring Boot Dependency Injection of the accountSystemFacade
     * @param accountSystemFacade the dependency to be injected
     */
    @SuppressWarnings("unused")
    @Autowired
    public AccountController(AccountSystemFacade accountSystemFacade) {
        this.accountSystemFacade = accountSystemFacade;
    }

    /**
     * Post request to log in with the given credentials
     * @param username of type String, username to reference associated account
     * @param password of type String, password to validate associated account
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    @PostMapping("/login" )
    public ResponseEntity<Object> loginAccount(@RequestParam String username, String password){
        // Run AccountManager service
        return this.accountSystemFacade.loginAccount(username, password);
    }

    /**
     * Post request to log out with the given credentials
     * @param sessionID of type String, sessionID to reference associated account
     * @return a response entity detailing successful completion or any associated error
     */
    @PostMapping("/logout" )
    public ResponseEntity<Object> logoutAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return this.accountSystemFacade.logoutAccount(new SessionID(sessionID));
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
        return this.accountSystemFacade.registerAccount(username, password);
    }

    /**
     * Delete request to delete the account associated with the given parameter
     * @param sessionID of type String, sessionID to reference the account to be deleted
     * @return a response entity detailing successful completion or any associated error
     */
    @DeleteMapping("/delete" )
    public ResponseEntity<Object> deleteAccount(@RequestParam String sessionID){
        // Run AccountManager service
        return this.accountSystemFacade.deleteAccount(new SessionID(sessionID));
    }

    /**
     * Patch request to update the account associated with the given parameter
     * @param sessionID of type String, sessionID to reference the account to be deleted
     * @param newUsername of type String, newUsername to change the account credential to
     * @return a response entity detailing successful completion or any associated error
     */
    @PatchMapping("/updateUsername" )
    public ResponseEntity<Object> updateAccountUsername(@RequestParam String sessionID, String newUsername){
        // Run AccountManager service
        return this.accountSystemFacade.updateUsername(new SessionID(sessionID), newUsername);
    }

    /**
     * Patch request to update the account associated with the given parameter
     * @param sessionID of type String, sessionID to reference the account to be deleted
     * @param newPassword of type String, newPassword to change the account credential to
     * @return a response entity detailing successful completion or any associated error
     */
    @PatchMapping("/updatePassword" )
    public ResponseEntity<Object> updateAccountPassword(@RequestParam String sessionID, String newPassword){
        // Run AccountManager service
        return this.accountSystemFacade.updatePassword(new SessionID(sessionID), newPassword);
    }

    /**
     * Get request to retrieve the information associated with the given parameter
     * @param sessionID of type String, sessionID to reference the associated account
     * @return a response entity detailing successful completion (with the information of the associated Protected Account entity) or any associated error
     */
    @GetMapping("/account" )
    public ResponseEntity<Object> getAccount(@RequestParam String sessionID) {
        // Run AccountManager service
        return this.accountSystemFacade.getAccountInfo(new SessionID(sessionID));
    }
}
