package com.backend.usecases.facades;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.AccountInfoException;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.HealthManager;
import com.backend.usecases.managers.PetManager;
import com.backend.usecases.managers.TaskManager;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Map;

@Service
public class AccountSystemFacade {
    private final AccountManager accountManager;
    private final PetManager petManager;
    private final HealthManager healthManager;
    private final TaskManager taskManager;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the accountManager
     *
     * @param accountManager the dependency to be injected
     * @param petManager     the dependency to be injected
     * @param healthManager  the dependency to be injected
     * @param taskManager    the dependency to be injected
     */
    @Autowired
    public AccountSystemFacade (AccountManager accountManager, PetManager petManager, HealthManager healthManager, TaskManager taskManager, IErrorHandler errorHandler) {
        this.accountManager = accountManager;
        this.petManager = petManager;
        this.healthManager = healthManager;
        this.taskManager = taskManager;
        this.errorHandler = errorHandler;
    }

    /**
     * Login to an account (reference DB) given account credentials
     * @param username of type String, username to reference and verify account details
     * @param password of type String, password to reference and verify account details
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    public ResponseEntity<Object> loginAccount(String username, String password) {
        // Verify Account Information
        if (!new Username(username).isValid() || !new Password(password).isValid()) {
            return errorHandler.logError(new AccountInfoException(String.format("The given account info is invalid, %s and %s", username, password)), HttpStatus.BAD_REQUEST);
        }

        // Hash the password
        String hashedPassword = this.accountManager.hash(password);

        // Get accountID
        Account account = this.accountManager.validateCredentials(username, hashedPassword);

        // Check for errors
        if (account == null) return errorHandler.logError(new AccountInfoException("Could not find matching account"), HttpStatus.NOT_FOUND);
        else if (account.getSessionID() != null) return errorHandler.logError(new SessionException("Already Logged in"), HttpStatus.UNAUTHORIZED);

        // Generate a session ID
        account.getSessionIDObject().generateID();
        account.updateData();

        // Save to DB
        this.accountManager.updateAccount(account);

        // Other Manager Calls
        this.healthManager.healthDecay(account.getAccountID(), this.taskManager.getTaskCompletionRecords(account.getAccountIDObject()));

        JSONObject returnObject = new JSONObject(Map.of("sessionID", account.getSessionIDObject().getID()));

        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    /**
     * Logout of an account (reference DB) given a valid sessionID
     * @param sessionID of type SessionID, sessionID to reference corresponding account to log out
     * @return a response entity detailing successful completion or any associated error
     */
    public ResponseEntity<Object> logoutAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = this.accountManager.verifySession(sessionID);

        // Check if the account exists
        if (accountID == null) return errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Logout account by the accountID
        Account account = this.accountManager.getAccount(accountID);
        account.getSessionIDObject().setID(null);

        account.updateData();

        this.accountManager.updateAccount(account);

        return new ResponseEntity<>("Successfully Logged out!", HttpStatus.OK);
    }

    /**
     * Register a new account (save to DB) given account credentials
     * @param username of type String, username to be saved in the newly created account
     * @param password of type String, password to be saved in the newly created account
     * @return a response entity detailing successful completion or any associated error
     */
    public ResponseEntity<Object> registerAccount(String username, String password) {
        // Create account instance with the given info
        Account account = this.accountManager.createAccount(username, password);

        // Validate created account
        if (!this.accountManager.verifyAccountInfo(account)) return errorHandler.logError(new AccountInfoException("The given account info is invalid"), HttpStatus.BAD_REQUEST);

        // Hash the password
        this.accountManager.hashPassword(account, password);

        // Check if the account exists
        if (this.accountManager.accountExists(account)) return errorHandler.logError(new AccountInfoException("The given account already exists!"), HttpStatus.BAD_REQUEST);

        // Save to DB
        this.accountManager.updateAccount(account);

        // Other Manager Calls
        this.petManager.initializePet(account.getAccountID());

        JSONObject returnObject = new JSONObject(Map.of("sessionID", account.getSessionIDObject().getID()));

        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    /**
     * Deletion of an account (remove from DB) given a valid sessionID
     * @param sessionID of type SessionID, sessionID to reference corresponding account to delete
     * @return a response entity detailing successful completion or any associated error
     */
    public ResponseEntity<Object> deleteAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = this.accountManager.verifySession(sessionID);

        // Check if the account exists
        if (accountID == null ) return errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Other manager calls
        this.petManager.deletePet(accountID.getID());

        // Delete account by the accountID
        if (!this.accountManager.deleteAccount(accountID)) {
            return errorHandler.logError(new AccountException("The given account could not be deleted"), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("Successfully Deleted Account!", HttpStatus.OK);
    }

    /**
     * Get the account information (restricted to protected account) associated with the given id
     * @param sessionID of type SessionID, the identifier used to reference account information
     * @return a new ProtectedAccount with information associated with the given id parameter
     */
    public ResponseEntity<Object> getAccountInfo(SessionID sessionID) {
        // Verify sessionID
        AccountID accountID = this.accountManager.verifySession(sessionID);

        // Check for errors
        if (accountID == null) {
            return errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        // Get account information
        ProtectedAccount protectedAccount = this.accountManager.getAccountInfo(accountID);

        if (protectedAccount == null) {
            return errorHandler.logError(new AccountNotFoundException("The account cannot be found with the given information"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(protectedAccount, HttpStatus.OK);
    }

    /**
     * Update account credentials associated with the given id and new username
     * @param sessionID of type SessionID, the identifier used to reference account information
     * @param newUsername of type String, newUsername to change the account credential to
     * @return a new ProtectedAccount with new information associated with the given id parameter
     */
    public ResponseEntity<Object> updateUsername(SessionID sessionID, String newUsername) {
        // Validate username
        Username newUsernameObject = new Username(newUsername);

        // Verify sessionID
        AccountID accountID = this.accountManager.verifySession(sessionID);

        // Check for errors
        if (accountID == null) {
            return errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }
        if (!newUsernameObject.isValid()) {
            return this.errorHandler.logError(new AccountInfoException("This is not a valid username"), HttpStatus.BAD_REQUEST);
        }

        // Get account information
        Account account = this.accountManager.getAccount(accountID);
        this.accountManager.updateAccount(new Account(accountID, newUsername, account.getPassword(), account.getTimestamp()));
        ProtectedAccount accountInformation = this.accountManager.getAccountInfo(accountID);

        if (accountInformation == null) {
            return errorHandler.logError(new AccountNotFoundException("The account cannot be found with the given information"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountInformation, HttpStatus.OK);
    }

    /**
     * Update account credentials associated with the given id and new password
     * @param sessionID of type SessionID, the identifier used to reference account information
     * @param newPassword of type String, newPassword to change the account credential to
     * @return a new ProtectedAccount with new information associated with the given id parameter
     */
    public ResponseEntity<Object> updatePassword(SessionID sessionID, String newPassword) {
        // Validate password
        Password newPasswordObject = new Password(newPassword);

        if (!newPasswordObject.isValid()) {
            return this.errorHandler.logError(new AccountInfoException("This is not a valid username"), HttpStatus.BAD_REQUEST);
        }

        // Verify sessionID
        AccountID accountID = this.accountManager.verifySession(sessionID);

        // Check for errors
        if (accountID == null) {
            return errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        // Get account information
        Account account = this.accountManager.getAccount(accountID);
        this.accountManager.updateAccount(new Account(accountID, account.getUsername(), this.accountManager.hash(newPassword), account.getTimestamp()));
        ProtectedAccount accountInformation = this.accountManager.getAccountInfo(accountID);

        if (accountInformation == null) {
            return errorHandler.logError(new AccountNotFoundException("The account cannot be found with the given information"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountInformation, HttpStatus.OK);
    }
}
