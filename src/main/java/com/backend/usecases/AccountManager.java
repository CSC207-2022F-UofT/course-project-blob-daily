package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.DBAccount;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.IDException;
import com.backend.error.exceptions.InvalidAccountInfoException;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Service
@Configurable
public class AccountManager {

    public static boolean verifyAccountInfo(ProtectedAccount account) {
        boolean isValid;

        // verify credentials
        isValid = account.getUsername().isValid();
        isValid &= account.getTimestamp() != null;

        if (account instanceof Account) {
            isValid &= ((Account) account).getAccountID().isValid();
            isValid &= ((Account) account).getSessionID().isValid();
            isValid &= ((Account) account).getPassword().isValid();
        }

        if(!isValid) LogHandler.logError(new InvalidAccountInfoException("The given account info is invalid"));

        return isValid;
    }

    public static AccountID verifySession(SessionID sessionID) {
        // Make DB call to find account based on id
        DBAccount account = AccountController.accountsRepo.findAccountID(sessionID.getID());

        // Check if found
        if (account == null) {
            LogHandler.logError(new SessionException("Invalid Session"));
            return null;
        }

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

    public static ProtectedAccount getAccountInfo(ID id) {
        // Verify sessionID
        if (id instanceof SessionID) {
            id = verifySession((SessionID) id);
        }

        if(id instanceof AccountID) {
            // Make DB call to find account based on id (check if exists aswell)
            if (!AccountController.accountsRepo.existsById(id.toString())) return null;

            DBAccount foundAccount = AccountController.accountsRepo.findByAccountID(id.toString());

            // package data into account instance
            return new ProtectedAccount(foundAccount.getUsername(), foundAccount.getTimestamp());

        } else {
            // return when error occurs
            if (id != null) LogHandler.logError(new IDException(String.format("Incorrect ID type: %s%n", id.getClass().toString())));
            return null;
        }
    }

    public static SessionID registerAccount(String username, String password) {
        // Create account instance with the given info
        Account newAccount = new Account(new AccountID(null), username, password, new Timestamp(System.currentTimeMillis()));

        // Generate required IDs
        newAccount.getAccountID().generateID();
        newAccount.getSessionID().generateID();

        // Validate created account
        if (!AccountManager.verifyAccountInfo(newAccount)) return null;

        // Hash the password
        newAccount.getPassword().setPassword(AccountManager.hash(password));

        // Save to DB
        DBAccount dbAcc = new DBAccount(newAccount);
        AccountController.accountsRepo.save(dbAcc);

        return newAccount.getSessionID();
    }

    public static SessionID loginAccount(String username, String password) {
        // Verify Account Information
        if (!new Username(username).isValid() || !new Password(password).isValid()) {
            LogHandler.logError(new InvalidAccountInfoException(String.format("The given account info is invalid, %s and %s", username, password)));
            return null;
        }

        // Hash the password
        password = AccountManager.hash(password);

        // Get accountID
        DBAccount account = AccountController.accountsRepo.findByCredentials(username, password);

        // Check if the account could be found
        if (account == null) {
            LogHandler.logError(new InvalidAccountInfoException("Could not find matching account"));
            return null;
        } else if (account.getSessionID() != null) {
            LogHandler.logError(new SessionException("Already Logged in"));
            return null;
        }

        // Generate a session ID
        SessionID newSessionID = new SessionID(null);
        newSessionID.generateID();
        account.setSessionID(newSessionID.getID());
        AccountController.accountsRepo.save(account);

        return newSessionID;
    }

    public static boolean logoutAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null) return false;

        // Delete account by the accountID
        DBAccount account = AccountController.accountsRepo.findByAccountID(accountID.getID());
        account.setSessionID(null);
        AccountController.accountsRepo.save(account);

        return true;
    }

    public static boolean deleteAccount(SessionID sessionID) {
        // Get accountID
        AccountID accountID = verifySession(sessionID);

        // Check if the account exists
        if (accountID == null ) return false;

        // Delete account by the accountID
        AccountController.accountsRepo.deleteById(accountID.getID());

        return !AccountController.accountsRepo.existsById(accountID.getID());
    }

    public boolean updateAccount(Account updatedAccount) {
        // verify updated account
        if (!AccountManager.verifyAccountInfo(updatedAccount) && AccountController.accountsRepo.existsById(updatedAccount.getAccountID().getID())) return false;

        // Convert to data representation
        DBAccount dbAccount = new DBAccount(updatedAccount);

        // Delete account by the accountID
        AccountController.accountsRepo.save(dbAccount);

        return true;
    }

}
