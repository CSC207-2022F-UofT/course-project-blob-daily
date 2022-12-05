package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

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

    @Transient
    @JsonIgnore
    private final AccountID receiverIDObject;

    @Id
    private final String ID;

    private final String senderID;
    private final String receiverID;
    private final Date timestamp;

    // fields, getters
    public Invitation(AccountID senderIDObject, AccountID receiverIDObject, Date timestamp) {
        this.ID = senderIDObject.getID() + receiverIDObject.getID();

        this.senderIDObject = senderIDObject;
        this.senderID = senderIDObject.getID();

        this.receiverIDObject = receiverIDObject;
        this.receiverID = receiverIDObject.getID();

        this.timestamp = timestamp;
    }
    @PersistenceCreator
    public Invitation(String ID, String senderID, String receiverID, Date timestamp) {
        this.ID = ID;
        this.senderIDObject = new AccountID(senderID);
        this.receiverIDObject = new AccountID(receiverID);
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.timestamp = timestamp;
    }

    // Getters

    /**
     * Retrieve the AccountID String as the sender
     * @return the AccountID String as the sender
     */
    public String getSenderID() {
        return this.senderID;
    }

    /**
     * Retrieve the AccountID String as the receiver
     * @return the AccountID String as the receiver
     */
    public String getReceiverID() {
        return this.receiverID;
    }

    /**
     * Retrieve the AccountID Object as the sender
     * @return the AccountID Object as the sender
     */
    public AccountID getSenderIDObject() {
        return this.senderIDObject;
    }

    /**
     * Retrieve the Account Object as the retriever
     * @return the Account Object as the retriever
     */
    public AccountID getReceiverIDObject() {
        return this.receiverIDObject;
    }

    /**
     * Retrieve the TimeStamp as java.util.Date
     * @return the TimeStamp as java.util.Date
     */
    public Date getTimestamp() {
        return this.timestamp;
    }
}
