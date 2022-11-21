package com.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "TaskCompletedCollections")
public class TaskCompletionRecord {
    @JsonIgnore
    private final String accountID;
    private final String username;
    private final Date timestamp;
    private final String taskName;

    @PersistenceCreator
    public TaskCompletionRecord(String accountID, String username, String taskName, Date timestamp){
        this.accountID = accountID;
        this.username = username;
        this.timestamp = timestamp;
        this.taskName = taskName;
    }

    @JsonIgnore
    public String getAccountID(){
        return this.accountID;
    }
    public Date getTimestamp(){
        return this.timestamp;
    }
    public String getTaskName(){
        return this.taskName;
    }
    public String getUsername() {
        return this.username;
    }
}
