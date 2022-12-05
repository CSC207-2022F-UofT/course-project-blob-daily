package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.AccountsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@Configurable
public class AccountManager {
    // Database Connection
    private final AccountsRepo accountsRepo;

    /**
     * Spring Boot Dependency Injection of the Accounts Repository
     * @param accountsRepo the dependency to be injected
     */
    @Autowired
    public AccountManager(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    /**
     * Verify the given account based on the implicit criteria provided within the account's criteria expressions
     * @param account of type ProtectedAccount to be verified of implicit validity
     * @return whether the given account has valid credentials
     */
    public boolean verifyAccountInfo(ProtectedAccount account) {
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
    public AccountID verifySession(SessionID sessionID) {
        // Make DB call to find account based on id
        Account account = this.accountsRepo.findBySessionID(sessionID.getID());

        // Check if found
        if (account == null) return null;

        // Return
        return new AccountID(this.accountsRepo.findBySessionID(sessionID.getID()).getAccountID());
    }

    /**
     * Find a corresponding AccountID given the parameter (username)
     * @param username of type String to reference corresponding AccountID
     * @return the associated AccountID if exists
     */
    public AccountID getAccountIDByUsername(String username) {
        // Make DB call to find account based on username
        Account account = this.accountsRepo.findByUsername(username);

        // Check if found
        if (account == null) return null;

        return account.getAccountIDObject();
    }

    /**
     * Hash the given string using the SHA-256 algorithm
     * @param string of type String to be hash by the function
     * @return a new String value corresponding to the hashed string parameter
     */
    public String hash(String string) {
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
     * @param accountID of type AccountID, the identifier used to reference account information
     * @return a new ProtectedAccount with information associated with the given id parameter
     */
    public ProtectedAccount getAccountInfo(AccountID accountID) {
        // Make DB call to find account based on id (check if exists as well)
        if (!this.accountsRepo.existsById(accountID.toString())) return null;

        Account foundAccount = this.accountsRepo.findByAccountID(accountID.toString());

        // package data into account instance
        return new ProtectedAccount(foundAccount.getUsername(), foundAccount.getTimestamp());
    }

    /**
     * Check if the given account exists by lookup of username in the accounts database
     * @param account of type Account, specified account for comparison
     * @return Whether the given account already exists in the DB
     */
    public boolean accountExists(Account account) {
        return accountsRepo.findByUsername(account.getUsername()) != null;
    }

    /**
     * Hash the given password and store into the given account
     * @param account of type Account, entity to store password
     * @param password of type String, password to be hashed (SHA-256 algorithm)
     */
    public void hashPassword(Account account, String password) {
        account.setPassword(this.hash(password));
        account.updateData();
    }

    /**
     * Will create a new instance of an Account with the given valid information with newly generated IDs.
     * @param username of type String,
     * @param password of type String,
     * @return new instance of account with given information
     */
    public Account createAccount(String username, String password) {
        // Create account instance with the given info
        Account newAccount = new Account(new AccountID(null), username, password, new Date(System.currentTimeMillis()));

        // Generate required IDs
        newAccount.getAccountIDObject().generateID();
        newAccount.getSessionIDObject().generateID();

        return newAccount;
    }

    /**
     * Validate given account credentials and return corresponding account
     * @param username of type String, username to reference and verify account details
     * @param hashedPassword of type String, the hashed password to reference and verify account details
     * @return the associated account for the given credentials
     */
    public Account validateCredentials(String username, String hashedPassword) {
        // Get account (if exists)
        return this.accountsRepo.findByCredentials(username, hashedPassword);
    }

    /**
     * Get the requested account associated with the sessionID
     * @param accountID of type AccountID, accountID to reference corresponding account to retrieve
     * @return a corrosponding account with the given accountID (if exists)
     */
    public Account getAccount(AccountID accountID) {
        return this.accountsRepo.findByAccountID(accountID.getID());
    }

    /**
     * Deletion of an account (remove from DB) given a valid sessionID
     * @param accountID of type AccountID, accountID to reference corresponding account to delete
     * @return Whether the deletion was successful
     */
    public boolean deleteAccount(AccountID accountID) {
        // Delete account by the accountID
        this.accountsRepo.deleteById(accountID.getID());

        return !this.accountsRepo.existsById(accountID.getID());
    }

    /**
     * Update the credentials/information of an existing account (overwrite DB).
     * @param updatedAccount an Account entity with updated credentials and AccountID of target account to be updated.
     */
    public void updateAccount(Account updatedAccount) {
        // save updated account the database
        this.accountsRepo.save(updatedAccount);
    }

}
