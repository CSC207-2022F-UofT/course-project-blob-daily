package com.backend.entities.users;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.info.Password;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents an Account Entity (secure information, password and ID information).
 */
@Document(collection = "AccountsCollection")
public class Account extends ProtectedAccount{
    // Instance Variables
    @Transient
    private final AccountID accountIDObject;

    @SuppressWarnings("unused")
    @Id
    private String accountID;
    @Transient
    private final SessionID sessionIDObject;
    @SuppressWarnings("unused")
    private String sessionID;
    @Transient
    private final Password passwordObject;
    @SuppressWarnings("unused")
    private String password;

    // Constructors
    public Account(AccountID accountID, String username, String password, Date timestamp) {
        super(username, timestamp);

        this.accountIDObject = accountID;
        this.accountID = accountID.getID();

        this.passwordObject = new Password(password);
        this.password = passwordObject.toString();

        this.sessionIDObject = new SessionID(null);
        this.sessionID = sessionIDObject.getID();
    }

    @PersistenceCreator
    @SuppressWarnings("unused")
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

    /**
     * Retrieve the AccountID Object for this given instance
     * @return the AccountID instance for this given instance
     */
    @Transient
    public AccountID getAccountIDObject() {
        return this.accountIDObject;
    }

    /**
     * Retrieve the AccountID string for this given instance
     * @return the AccountID string representation for this given instance
     */
    public String getAccountID() {
        return this.accountIDObject.getID();
    }

    /**
     * Retrieve the SessionID Object for this given instance
     * @return the SessionID instance for this given instance
     */
    @Transient
    public SessionID getSessionIDObject() {
        return this.sessionIDObject;
    }

    /**
     * Retrieve the SessionID string for this given instance
     * @return the SessionID string representation for this given instance
     */
    public String getSessionID() {
        return this.sessionIDObject.getID();
    }

    /**
     * Retrieve the Password Object for this given instance
     * @return the Password instance for this given instance
     */
    @Transient
    public Password getPasswordObject() {
        return this.passwordObject;
    }

    /**
     * Retrieve the Password string for this given instance
     * @return the Password string representation for this given instance
     */
    public String getPassword() {
        return this.passwordObject.toString();
    }

    // Setter

    /**
     * Set the current password to the given parameter
     * @param newPassword of type String, newPassword to set the current password to
     */
    public void setPassword(String newPassword) {
        this.passwordObject.setPassword(newPassword);
    }

    /**
     * Update the current data type for Mongo Repository
     */
    public void updateData() {
        this.sessionID = this.sessionIDObject.getID();
        this.accountID = this.accountIDObject.getID();
        this.password = this.passwordObject.toString();
    }

}
