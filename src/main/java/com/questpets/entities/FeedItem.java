package com.questpets.entities;


import com.questpets.entities.users.ProtectedAccount;

public class FeedItem {
    //Attributes
    private final ProtectedAccount account;
    private final Pet pet;
    private final TaskCompletionRecord record;

    // Constructor
    public FeedItem(ProtectedAccount account, TaskCompletionRecord record, Pet pet){
        this.account = account;
        this.record = record;
        this.pet = pet;
    }
}
