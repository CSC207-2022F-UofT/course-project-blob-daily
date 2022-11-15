package com.questpets.entities.users;

import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.users.info.Password;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "AccountsCollection")
public class Account extends ProtectedAccount{
    // Instance Variables
    private final AccountID accountID;
    private final SessionID sessionID;
    private final Password password;

    // Constructors
    public Account(AccountID accountID, String username, String password, Timestamp timestamp) {
        super(username, timestamp);
        this.accountID = accountID;
        this.password = new Password(password);
        this.sessionID = new SessionID(null);
    }

    public Account(SessionID sessionID, String username, String password) {
        super(username);
        this.sessionID = sessionID;
        this.password = new Password(password);
        this.accountID = new AccountID(null);
    }

    // Getters
    public AccountID getAccountID() {
        return this.accountID;
    }

    public SessionID getSessionID() {
        return this.sessionID;
    }

    public Password getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password.setPassword(newPassword);
    }

}
