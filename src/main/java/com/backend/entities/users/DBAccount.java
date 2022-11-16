package com.backend.entities.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "AccountsCollection")
public class DBAccount {
    private String username;
    private String password;
    @Id
    private final String accountID;
    private String sessionID;

    private Timestamp timestamp;

    public DBAccount(Account account) {
        this.username = account.getUsername().toString();
        this.password = account.getPassword().toString();
        this.accountID = account.getAccountID().toString();
        this.sessionID = account.getSessionID().toString();
        this.timestamp = account.getTimestamp();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
