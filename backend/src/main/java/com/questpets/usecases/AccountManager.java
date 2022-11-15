package com.questpets.usecases;



import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.ID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.users.ProtectedAccount;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class AccountManager {

    public boolean verifyAccountInfo(ProtectedAccount account) {
        throw new UnsupportedOperationException();
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

    public ProtectedAccount getAccountInfo(ID id) {
        if(id instanceof AccountID) {
            throw new UnsupportedOperationException();
        }else if(id instanceof SessionID) {
            throw new UnsupportedOperationException();
        }else {
            System.out.printf("Incorrect ID type: %s%n", id.getClass().toString());
            return null;
        }
    }

    public SessionID registerAccount() {
        throw new UnsupportedOperationException();
    }

    public SessionID loginAccount() {
        throw new UnsupportedOperationException();
    }

    public boolean logoutAccount() {
        throw new UnsupportedOperationException();
    }

    public boolean deleteAccount() {
        throw new UnsupportedOperationException();
    }

    public boolean updateAccount() {
        throw new UnsupportedOperationException();
    }

}
