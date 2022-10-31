package backend.entities;

import java.sql.Timestamp;

public class Invitation {
    // The value is generated by Account.java
    private final String senderID;

    // The value is generated by Account.java
    private final String receiverID;

    private final Timestamp timestamp;

    // fields, getters
    public Invitation(String senderID, String receiverID, Timestamp timestamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.timestamp = timestamp;
    }

    public String getSenderID() {
        return this.senderID;
    }

    public String getReceiverID() {
        return this.receiverID;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }
}