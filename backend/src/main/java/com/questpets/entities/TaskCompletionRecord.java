package com.questpets.entities;


import com.questpets.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "TaskCompletedCollection")
public class TaskCompletionRecord {
    private AccountID accountID;
    private Timestamp timestamp;
    private String task;
    private String image;

    public TaskCompletionRecord(AccountID accountID, Timestamp timestamp, String task, String image){
        this.accountID = accountID;
        this.timestamp = timestamp;
        this.task = task;
        this.image = image;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public AccountID getAccountID(){
        return this.accountID;
    }
    public Timestamp getTimestamp(){
        return this.timestamp;
    }
    public String getTask(){
        return this.task;
    }
    public String getImage() {
        return this.image;
    }
}
