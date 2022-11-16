package com.backend.entities.users;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.info.Password;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "AccountsCollection")
public class Account extends ProtectedAccount{
    // Instance Variables
    @Transient
    private final AccountID accountIDObject;
    @Id
    private String accountID;
    @Transient
    private final SessionID sessionIDObject;
    private String sessionID;
    @Transient
    private final Password passwordObject;
    private String password;

    // Constructors
    public Account(AccountID accountID, String username, String password, Timestamp timestamp) {
        super(username, timestamp);

        this.accountIDObject = accountID;
        this.accountID = accountID.getID();

        this.passwordObject = new Password(password);
        this.password = passwordObject.toString();

        this.sessionIDObject = new SessionID(null);
        this.sessionID = sessionIDObject.getID();
    }

    @PersistenceCreator
    public Account(String accountID, String sessionID, String username, String password, Date timestamp) {
        super(username, timestamp);

        this.accountID = accountID;
        this.accountIDObject = new AccountID(accountID);

        this.passwordObject = new Password(password);
        this.password = passwordObject.toString();

        this.sessionIDObject = new SessionID(sessionID);
        this.sessionID = sessionIDObject.getID();
    }

    public Account(SessionID sessionID, String username, String password) {
        super(username);

        this.sessionIDObject = sessionID;
        this.sessionID = sessionIDObject.getID();

        this.passwordObject = new Password(password);
        this.password = passwordObject.toString();

        this.accountIDObject = new AccountID(null);
        this.accountID = accountIDObject.getID();
    }

    // Getters
    @Transient
    public AccountID getAccountIDObject() {
        return this.accountIDObject;
    }

    public String getAccountID() {
        return this.accountIDObject.getID();
    }

    @Transient
    public SessionID getSessionIDObject() {
        return this.sessionIDObject;
    }

    public String getSessionID() {
        return this.sessionIDObject.getID();
    }

    @Transient
    public Password getPasswordObject() {
        return this.passwordObject;
    }

    public String getPassword() {
        return this.passwordObject.toString();
    }

    public void setPassword(String newPassword) {
        this.passwordObject.setPassword(newPassword);
    }

    public void updateData() {
        this.sessionID = this.sessionIDObject.getID();
        this.accountID = this.accountIDObject.getID();
        this.password = this.passwordObject.toString();
    }

}
