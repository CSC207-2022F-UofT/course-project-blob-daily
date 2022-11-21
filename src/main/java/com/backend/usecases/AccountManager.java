package com.backend.usecases;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.AccountInfoException;
import com.backend.error.exceptions.IDException;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.AccountsRepo;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;

@Service
@Configurable
public class AccountManager {
    // Database Connection
    public static AccountsRepo accountsRepo;

    public AccountManager(AccountsRepo accountsRepo) {
        AccountManager.accountsRepo = accountsRepo;
    }

    /**
     * Verify the given account based on the implicit criteria provided within the account's criteria expressions
     * @param account of type ProtectedAccount to be verified of implicit validity
     * @return whether the given account has valid credentials
     */
    public static boolean verifyAccountInfo(ProtectedAccount account) {
        boolean isValid;

        // verify credentials
        isValid = account.getUsernameObject().isValid();
        isValid &= account.getTimestamp() != null;

        if (account instanceof Account) {
            isValid &= ((Account) account).getAccountIDObject().isValid();
            isValid &= ((Account) account).getSessionIDObject().isValid();
            isValid &= ((Account) account).getPasswordObject().isValid();
        }

        return isValid;
    }

    /**
     * Verify the given sessionID to be a valid sessionID from the records held in the account repository
     * @param sessionID of type SessionID to check validity based on account database records
     * @return whether the given sessionID is valid based on the existent accounts within the account database records
     */
    public static AccountID verifySession(SessionID sessionID) {
        // Make DB call to find account based on id
        Account account = accountsRepo.findBySessionID(sessionID.getID());

        // Check if found
        if (account == null) return null;

        // Return
        return new AccountID(accountsRepo.findBySessionID(sessionID.getID()).getAccountID());
    }

    /**
     * Find a corresponding AccountID given the parameter (username)
     * @param username of type String to reference corresponding AccountID
     * @return the associated AccountID if exists
     */
    @SuppressWarnings("unused")
    public static AccountID getAccountIDByUsername(String username) {
        // Make DB call to find account based on username
        Account account = accountsRepo.findByUsername(username);

        // Check if found
        if (account == null) return null;

        return account.getAccountIDObject();
    }

    /**
     * Hash the given string using the SHA-256 algorithm
     * @param string of type String to be hash by the function
     * @return a new String value corresponding to the hashed string parameter
     */
    public static String hash(String string) {
        // Hash given String (SHA-256)
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] encodedHash = digest.digest(
                string.getBytes(StandardCharsets.UTF_8));

        // Convert to Hex
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Get the account information (restricted to protected account) associated with the given id
     * @param id of type ID, the identifier used to reference account information
     * @return a new ProtectedAccount with information associated with the given id parameter
     */
    public static ResponseEntity<Object> getAccountInfo(ID id) {
        // Verify sessionID
        if (id instanceof SessionID) {
            id = verifySession((SessionID) id);
        }

        if(id instanceof AccountID) {
            // Make DB call to find account based on id (check if exists as well)
            if (!accountsRepo.existsById(id.toString())) return LogHandler.logError(new IDException("Could not find account from given ID"), HttpStatus.NOT_FOUND);

            Account foundAccount = accountsRepo.findByAccountID(id.toString());

            // package data into account instance
            return new ResponseEntity<>(new ProtectedAccount(foundAccount.getUsername(), foundAccount.getTimestamp()), HttpStatus.OK);

        } else {
            // return when error occurs
            if (id != null) return LogHandler.logError(new IDException(String.format("Incorrect ID type: %s%n", id.getClass().toString())), HttpStatus.BAD_REQUEST);
            else return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Register a new account (save to DB) given account credentials
     * @param username of type String, username to be saved in the newly created account
     * @param password of type String, password to be saved in the newly created account
     * @return a response entity detailing successful completion or any associated error
     */
    public static ResponseEntity<Object> registerAccount(String username, String password) {
        // Create account instance with the given info
        Account newAccount = new Account(new AccountID(null), username, password, new Timestamp(System.currentTimeMillis()));

        // Generate required IDs
        newAccount.getAccountIDObject().generateID();
        newAccount.getSessionIDObject().generateID();

        // Validate created account
        if (!AccountManager.verifyAccountInfo(newAccount)) return LogHandler.logError(new AccountInfoException("The given account info is invalid"), HttpStatus.BAD_REQUEST);

        // Hash the password
        newAccount.setPassword(AccountManager.hash(password));

        newAccount.updateData();

        if (accountsRepo.findByUsername(username) != null) return LogHandler.logError(new AccountInfoException("The given account already exists!"), HttpStatus.BAD_REQUEST);

        // Save to DB
        accountsRepo.save(newAccount);

        JSONObject returnObject = new JSONObject(Map.of("sessionID", newAccount.getSessionIDObject().getID()));

        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    /**
     * Login to an account (reference DB) given account credentials
     * @param username of type String, username to reference and verify account details
     * @param password of type String, password to reference and verify account details
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    public static ResponseEntity<Object> loginAccount(String username, String password) {
        // Verify Account Information
        if (!new Username(username).isValid() || !new Password(password).isValid()) {
            return LogHandler.logError(new AccountInfoException(String.format("The given account info is invalid, %s and %s", username, password)), HttpStatus.BAD_REQUEST);
        }

        // Hash the password
        password = AccountManager.hash(password);

        // Get accountID
        Account account = accountsRepo.findByCredentials(username, password);

        // Check if the account could be found
        if (account == null) return LogHandler.logError(new AccountInfoException("Could not find matching account"), HttpStatus.NOT_FOUND);
        else if (account.getSessionID() != null) return LogHandler.logError(new SessionException("Already Logged in"), HttpStatus.UNAUTHORIZED);

        // Generate a session ID
        SessionID newSessionID = new SessionID(null);
        newSessionID.generateID();
        account.getSessionIDObject().setID(newSessionID.getID());

        account.updateData();

        accountsRepo.save(account);

        JSONObject returnObject = new JSONObject(Map.of("sessionID", newSessionID.getID()));

        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    /**
     * Logout of an account (reference DB) given a valid sessionID
     * @param sessionID of type SessionID, sessionID to reference corresponding account to log out
     * @return a response entity detailing successful completion or any associated error
     */
    public static ResponseEntity<Object> logoutAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null) return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Delete account by the accountID
        Account account = accountsRepo.findByAccountID(accountID.getID());
        account.getSessionIDObject().setID(null);

        account.updateData();

        accountsRepo.save(account);

        return new ResponseEntity<>("Successfully Logged out!", HttpStatus.OK);
    }

    /**
     * Deletion of an account (remove from DB) given a valid sessionID
     * @param sessionID of type SessionID, sessionID to reference corresponding account to delete
     * @return a response entity detailing successful completion or any associated error
     */
    public static ResponseEntity<Object> deleteAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null ) return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Delete account by the accountID
        accountsRepo.deleteById(accountID.getID());

        return new ResponseEntity<>("Successfully Deleted Account!", HttpStatus.OK);
    }

    /**
     * Update the credentials/information of an existing account (overwrite DB).
     * @param updatedAccount an Account entity with updated credentials and AccountID of target account to be updated.
     * @return a response entity detailing successful completion or any associated error
     */
    @Deprecated
    public boolean updateAccount(Account updatedAccount) {
        // verify updated account
        if (!AccountManager.verifyAccountInfo(updatedAccount) && accountsRepo.existsById(updatedAccount.getAccountIDObject().getID())) return false;

        // Delete account by the accountID
        accountsRepo.save(updatedAccount);

        return true;
    }

}
