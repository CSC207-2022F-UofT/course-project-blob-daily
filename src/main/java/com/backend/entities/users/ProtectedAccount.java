package com.backend.entities.users;

import com.backend.entities.users.info.Username;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents a Protected Account Entity (simple information, username and last logout).
 */
@Document(collection = "AccountsCollection")
public class ProtectedAccount {
    // Instance Variables
    @Transient
    @JsonIgnore
    private final Username usernameObject;
    @SuppressWarnings("unused")
    private final String username;
    private final Date timestamp;

    // Constructors
    @PersistenceCreator
    public ProtectedAccount(String username, Date timestamp) {
        this.usernameObject = new Username(username);
        this.username = this.usernameObject.toString();
        this.timestamp = timestamp;
    }

    public ProtectedAccount(String username) {
        this.usernameObject = new Username(username);
        this.username = this.usernameObject.toString();
        this.timestamp = new Date(System.currentTimeMillis());
    }

    // Getters

    /**
     * Retrieve the username Object for this given instance
     * @return the username instance for this given instance
     */
    @Transient
    public Username getUsernameObject() {
        return this.usernameObject;
    }

    /**
     * Retrieve the username string for this given instance
     * @return the username string representation for this given instance
     */
    public String getUsername() {
        return this.usernameObject.toString();
    }

    /**
     * Retrieve the timestamp for this given instance
     * @return the timestamp for this given instance
     */
    public Date getTimestamp() {
        return this.timestamp;
    }
}
