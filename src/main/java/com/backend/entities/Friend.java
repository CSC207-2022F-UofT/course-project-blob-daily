package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "FriendsCollection")
public class Friend {
    @Transient
    private final AccountID accountIDObject;
    @Id
    private String accountID;

    private ArrayList<String> friends;

    // fields, getters, setters
    public Friend(AccountID accountID, ArrayList<String> friends) {
        this.accountIDObject = accountID;
        this.accountID = accountID.getID();
        this.friends = friends;
    }

    @PersistenceCreator
    public Friend(String accountID, ArrayList<String> friends) {
        this.accountIDObject = new AccountID(accountID);
        this.accountID = accountID;
        this.friends = friends;
    }

    public String getAccountID() { return this.accountID; }
    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

}
