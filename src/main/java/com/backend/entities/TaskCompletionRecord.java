package com.backend.entities;


import com.backend.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * completion record for tasks
 */
@Document(collection = "TaskCompletedCollection")
public class TaskCompletionRecord {
    //instance variables
    @SuppressWarnings("unused")
    @Id
    private String ID;
    @Transient
    private final AccountID accountIDObject;
    private final String accountID;
    private final String timestamp;
    private final String task;
    private final String image;

    //constructors
    public TaskCompletionRecord(AccountID accountIDObject, String timestamp, String task, String image){
        this.accountIDObject = accountIDObject;
        this.accountID = accountIDObject.getID();
        this.timestamp = timestamp;
        this.task = task;
        this.image = image;
    }

    @PersistenceCreator
    public TaskCompletionRecord(String accountID, String timestamp, String task, String image) {
        this.accountIDObject = new AccountID(accountID);
        this.accountID = accountID;
        this.timestamp = timestamp;
        this.task = task;
        this.image = image;
    }

    //getters
    /**
     * get the unique ID
     * @return a string of ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * gets the accountID object
     * @return an accountID that completed the task
     */
    @Transient
    public AccountID getAccountIDObject(){
        return this.accountIDObject;
    }

    /**
     * gets the accountID
     * @return a string of the accountID
     */
    public String getAccountID() {
        return this.accountID;
    }

    /**
     * gets the timestamp
     * @return a string of the timestamp
     */
    public String getTimestamp(){
        return this.timestamp;
    }

    /**
     * gets the task name
     * @return a string of the task name
     */
    public String getTask(){
        return this.task;
    }

    /**
     * gets the link to the image
     * @return a string of the link to the image
     */
    public String getImage() {
        return this.image;
    }
}
