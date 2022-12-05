package com.backend.entities;


import com.backend.entities.IDs.AccountID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * completion record for tasks
 */
@Document(collection = "TaskCompletedCollection")
public class TaskCompletionRecord {
    //instance variables
    @Id
    @JsonIgnore
    private String ID;
    @Transient
    @JsonIgnore
    private final AccountID accountIDObject;
    @JsonIgnore
    private final String accountID;
    private final Date date;
    private final String name;
    private final String image;

    /**
     * Task completion record constructor
     * @param accountIDObject of type AccountID, the account that completed the task
     * @param date of type Date, the date the task was completed
     * @param name of type String, the name of the task
     * @param image of type String, the link of the image
     */
    public TaskCompletionRecord(AccountID accountIDObject, Date date, String name, String image){
        this.accountIDObject = accountIDObject;
        this.accountID = accountIDObject.getID();
        this.date = date;
        this.name = name;
        this.image = image;
    }

    /**
     * Task completion record constructor
     * @param accountID of type String, the account that completed the task
     * @param date of type Date, the date the task was completed
     * @param name of type String, the name of the task
     * @param image of type String, the link of the image
     */
    @PersistenceCreator
    public TaskCompletionRecord(String accountID, Date date, String name, String image) {
        this.accountIDObject = new AccountID(accountID);
        this.accountID = accountID;
        this.date = date;
        this.name = name;
        this.image = image;
    }

    /**
     * get the unique ID
     * @return a string of ID
     */
    @JsonIgnore
    public String getID() {
        return this.ID;
    }

    /**
     * gets the accountID object
     * @return an accountID that completed the task
     */
    @Transient
    @JsonIgnore
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
    public String getDate(){
        return this.date.toString().substring(0,10);
    }

    /**
     * gets the task name
     * @return a string of the task name
     */
    public String getName(){
        return this.name;
    }

    /**
     * gets the link to the image
     * @return a string of the link to the image
     */
    public String getImage() {
        return this.image;
    }
}