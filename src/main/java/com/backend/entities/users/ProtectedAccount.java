package com.backend.entities.users;

import com.backend.entities.users.info.Username;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.minidev.json.JSONObject;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "AccountsCollection")
public class ProtectedAccount {
    // Instance Variables
    @Transient
    @JsonIgnore
    private final Username usernameObject;
    private final String username;
    private final Date timestamp;

    // Constructors
    public ProtectedAccount(String username, Timestamp timestamp) {
        this.usernameObject = new Username(username);
        this.username = this.usernameObject.toString();
        this.timestamp = timestamp;
    }

    @PersistenceCreator
    public ProtectedAccount(String username, Date timestamp) {
        this.usernameObject = new Username(username);
        this.username = this.usernameObject.toString();
        this.timestamp = new Timestamp(timestamp.getTime());
    }

    public ProtectedAccount(String username) {
        this.usernameObject = new Username(username);
        this.username = this.usernameObject.toString();
        this.timestamp = null;
    }

    // Getters
    @Transient
    public Username getUsernameObject() {
        return this.usernameObject;
    }

    public String getUsername() {
        return this.usernameObject.toString();
    }

    public Timestamp getTimestamp() {
        return new Timestamp(timestamp.getTime());
    }
}
