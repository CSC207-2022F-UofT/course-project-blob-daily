package com.questpets.entities;


import com.questpets.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "TaskCompletionCollections")
public class TaskCompletionRecord {
    @Id
    private AccountID accountID;
    private Timestamp timestamp;
    private String taskName;
    private String image;

    public TaskCompletionRecord(AccountID accountID, Timestamp timestamp, String taskName, String image){
        this.accountID = accountID;
        this.timestamp = timestamp;
        this.taskName = taskName;
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
        return this.taskName;
    }
    public String getImage() {
        return this.image;
    }
}
