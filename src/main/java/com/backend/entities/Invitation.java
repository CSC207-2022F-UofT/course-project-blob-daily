package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents an Invitation entity
 */
@Document(collection = "InvitationsCollection")
public class Invitation {
    // The value is generated by Account.java
    @Transient
    @JsonIgnore
    private final AccountID senderIDObject;

    // The value is generated by Account.java
    @Transient
    @JsonIgnore
    private final AccountID receiverIDObject;

    @Id
    private final String ID;

    private final String senderID;
    private final String receiverID;


    private final Date timestamp;

    // fields, getters
    public Invitation(String senderID, String receiverID, Date timestamp) {
        this.ID = senderID + receiverID;
        this.senderIDObject = new AccountID(senderID);
        this.receiverIDObject = new AccountID(receiverID);
        this.senderID = this.senderIDObject.toString();
        this.receiverID = this.receiverIDObject.toString();
        this.timestamp = timestamp;
    }
    @PersistenceCreator
    public Invitation(String ID, String senderID, String receiverID, Date timestamp) {
        this.ID = ID;
        this.senderIDObject = new AccountID(senderID);
        this.receiverIDObject = new AccountID(receiverID);
        this.senderID = this.senderIDObject.toString();
        this.receiverID = this.receiverIDObject.toString();
        this.timestamp = timestamp;
    }

    /**
     * Retrieve the AccountID of the sender for this given instance
     * @return the AccountID of the sender for this given instance
     */
    public AccountID getSenderIDObject() {
        return this.senderIDObject;
    }

    /**
     * Retrieve the AccountID of the receiver for this given instance
     * @return the AccountID of the receiver for this given instance
     */

    public AccountID getReceiverIDObject() {
        return this.receiverIDObject;
    }

    /**
     * Retrieve the String of the sender for this given instance
     * @return the String of the sender for this given instance
     */
    public String getSenderID(){
        return this.senderID;
    }

    /**
     * Retrieve the String of the receiver for this given instance
     * @return the String of the receiver for this given instance
     */
    public String getReceiverID(){
        return this.receiverID;
    }

    /**
     * Retrive the java.util.Date of the invitation
     * @return the java.u
     */
    public Date getTimestamp() {
        return this.timestamp;
    }
}
