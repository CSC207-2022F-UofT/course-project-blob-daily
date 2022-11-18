package com.backend.entities.users;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.info.Password;

import java.sql.Timestamp;

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
