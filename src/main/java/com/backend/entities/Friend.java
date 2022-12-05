package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * Represents a Friend Entity (accountID, friendsList)
 */
@Document(collection = "FriendsCollection")
public class Friend {
    @Transient
    private final AccountID accountIDObject;
    @Id
    private final String accountID;

    private final ArrayList<String> friends;

    // fields, getters, setters
    public Friend(AccountID accountIDObject, ArrayList<String> friends) {
        this.accountIDObject = accountIDObject;
        this.accountID = accountIDObject.getID();
        this.friends = friends;
    }

    @PersistenceCreator
    public Friend(String accountID, ArrayList<String> friends) {
        this.accountIDObject = new AccountID(accountID);
        this.accountID = accountID;
        this.friends = friends;
    }

    /**
     * Retrieve the AccountID as String for this given instance
     * @return the AccountID as String for this given instance
     */
    @SuppressWarnings("unused")
    public AccountID getAccountIDObject() {
        return this.accountIDObject;
    }

    /**
     * Retrieve the AccountID as String for this given instance
     * @return the AccountID as String for this given instance
     */
    public String getAccountID() { return this.accountID; }

    /**
     * Retrieves an array of the user's friends
     * @return an array of the user's friends
     */
    public ArrayList<String> getFriends() {
        return this.friends;
    }

}
