package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.DBAccount;
import com.backend.entities.users.ProtectedAccount;
import com.backend.error.exceptions.IDException;
import com.backend.error.handlers.LogHandler;
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

        return isValid;
    }

    public static AccountID verifySession(SessionID sessionID) {
        // Make DB call to find account based on id
        return new AccountID(AccountController.accountsRepo.findByAccountID(sessionID.getID()).getAccountID());
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
        if (id instanceof SessionID) {
            id = verifySession((SessionID) id);
        }

        if(id instanceof AccountID) {
            // Make DB call to find account based on id
            DBAccount foundAccount = AccountController.accountsRepo.findByAccountID(id.toString());
            // package data into account instance
            return new ProtectedAccount(foundAccount.getUsername(), foundAccount.getTimestamp());
        } else {
            LogHandler.logError(new IDException(String.format("Incorrect ID type: %s%n", id.getClass().toString())));
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
        newAccount.getPassword().setPassword(AccountManager.hash(password));
        // Save to DB
        DBAccount dbAcc = new DBAccount(newAccount);

        AccountController.accountsRepo.save(dbAcc);

        return newAccount.getSessionID();
    }

    public SessionID loginAccount() {
        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return null;
    }

    public boolean logoutAccount() {
        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return false;
    }

    public boolean deleteAccount() {
        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return false;
    }

    public boolean updateAccount() {
        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return false;
    }

    public static void main(String[] args) {
        ProtectedAccount a = new ProtectedAccount("ShaanP22", new Timestamp(System.currentTimeMillis()));
        Account p = new Account(new AccountID(""), null, null, new Timestamp(System.currentTimeMillis()));

        p.getAccountID().generateID();
        p.getSessionID().generateID();
        p.getPassword().generatePassword();
        p.getUsername().generateUsername();

        System.out.println(AccountManager.verifyAccountInfo(a));
    }

}
