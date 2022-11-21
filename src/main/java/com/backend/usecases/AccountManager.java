package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.IDException;
import com.backend.error.exceptions.AccountInfoException;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
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

    public static AccountID verifySession(SessionID sessionID) {
        // Make DB call to find account based on id
        Account account = AccountController.accountsRepo.findAccountID(sessionID.getID());

        // Check if found
        if (account == null) return null;

        // Return
        return new AccountID(AccountController.accountsRepo.findAccountID(sessionID.getID()).getAccountID());
    }

    // Hash the given string using the SHA-256 algorithm, return hexadecimal value
    public static String hash(String string) {
        // Hash given String (SHA-256)
        MessageDigest digest = null;
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

    public static ResponseEntity<Object> getAccountInfo(ID id) {
        // Verify sessionID
        if (id instanceof SessionID) {
            id = verifySession((SessionID) id);
        }

        if(id instanceof AccountID) {
            // Make DB call to find account based on id (check if exists aswell)
            if (!AccountController.accountsRepo.existsById(id.toString())) return LogHandler.logError(new IDException("Could not find account from given ID"), HttpStatus.NOT_FOUND);

            Account foundAccount = AccountController.accountsRepo.findByAccountID(id.toString());

            // package data into account instance
            return new ResponseEntity<>(new ProtectedAccount(foundAccount.getUsername(), foundAccount.getTimestamp()), HttpStatus.OK);

        } else {
            // return when error occurs
            if (id != null) return LogHandler.logError(new IDException(String.format("Incorrect ID type: %s%n", id.getClass().toString())), HttpStatus.BAD_REQUEST);
            else return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }
    }

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

        if (AccountController.accountsRepo.findByUsername(username) != null) return LogHandler.logError(new AccountInfoException("The given account already exists!"), HttpStatus.BAD_REQUEST);

        // Save to DB
        AccountController.accountsRepo.save(newAccount);

        JSONObject returnObject = new JSONObject(Map.of("sessionID", newAccount.getSessionIDObject().getID()));

        return new ResponseEntity<Object>(returnObject, HttpStatus.OK);
    }

    public static ResponseEntity<Object> loginAccount(String username, String password) {
        // Verify Account Information
        if (!new Username(username).isValid() || !new Password(password).isValid()) {
            return LogHandler.logError(new AccountInfoException(String.format("The given account info is invalid, %s and %s", username, password)), HttpStatus.BAD_REQUEST);
        }

        // Hash the password
        password = AccountManager.hash(password);

        // Get accountID
        Account account = AccountController.accountsRepo.findByCredentials(username, password);

        // Check if the account could be found
        if (account == null) return LogHandler.logError(new AccountInfoException("Could not find matching account"), HttpStatus.NOT_FOUND);
        else if (account.getSessionID() != null) return LogHandler.logError(new SessionException("Already Logged in"), HttpStatus.UNAUTHORIZED);

        // Generate a session ID
        SessionID newSessionID = new SessionID(null);
        newSessionID.generateID();
        account.getSessionIDObject().setID(newSessionID.getID());

        account.updateData();

        AccountController.accountsRepo.save(account);

        JSONObject returnObject = new JSONObject(Map.of("sessionID", newSessionID.getID()));

        return new ResponseEntity<Object>(returnObject, HttpStatus.OK);
    }

    public static ResponseEntity<Object> logoutAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null) return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Delete account by the accountID
        Account account = AccountController.accountsRepo.findByAccountID(accountID.getID());
        account.getSessionIDObject().setID(null);

        account.updateData();

        AccountController.accountsRepo.save(account);

        return new ResponseEntity<Object>("Successfully Logged out!", HttpStatus.OK);
    }

    public static ResponseEntity<Object> deleteAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null ) return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);

        // Delete account by the accountID
        AccountController.accountsRepo.deleteById(accountID.getID());

        return new ResponseEntity<Object>("Successfully Deleted Account!", HttpStatus.OK);
    }

    public boolean updateAccount(Account updatedAccount) {
        // verify updated account
        if (!AccountManager.verifyAccountInfo(updatedAccount) && AccountController.accountsRepo.existsById(updatedAccount.getAccountIDObject().getID())) return false;

        // Delete account by the accountID
        AccountController.accountsRepo.save(updatedAccount);

        return true;
    }

    /**
     * Find a corresponding AccountID given the parameter (username)
     * @param username of type String to reference corresponding AccountID
     * @return the associated AccountID if exists
     */
    @SuppressWarnings("unused")
    public static AccountID getAccountIDByUsername(String username) {
        // Make DB call to find account based on username
        Account account = AccountController.accountsRepo.findByUsername(username);

        // Check if found
        if (account == null) return null;

        return account.getAccountIDObject();
    }

}