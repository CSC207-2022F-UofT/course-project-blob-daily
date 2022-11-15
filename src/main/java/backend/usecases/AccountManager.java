package backend.usecases;

import backend.entities.IDs.AccountID;
import backend.entities.IDs.ID;
import backend.entities.IDs.SessionID;
import backend.entities.users.Account;
import backend.entities.users.ProtectedAccount;
import backend.error.exceptions.IDException;
import backend.error.handlers.LogHandler;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

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

    // Hash the given string using the SHA-256 algorithm, return hexadecimal value
    public static String hash(String string) throws NoSuchAlgorithmException {
        // Hash given String (SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
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
            // Make DB call to verify session to get account ID
        }

        if(id instanceof AccountID) {
            // Make DB call to find account based on id
            // package data into account instance
        }else {
            LogHandler.logError(new IDException(String.format("Incorrect ID type: %s%n", id.getClass().toString())));
            return null;
        }

        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return null;
    }

    public SessionID registerAccount(String username, String password) {
        // Create account instance with the given info
        // Generate required IDs
        // Validate created account
        // Save to DB

        // Not implemented yet
        LogHandler.logError(new UnsupportedOperationException());
        return null;
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
