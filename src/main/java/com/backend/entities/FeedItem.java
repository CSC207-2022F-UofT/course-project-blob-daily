package com.backend.entities;

import com.backend.entities.users.ProtectedAccount;

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

    public ProtectedAccount getAccount() {
        return this.account;
    }

    public Pet getPet() {
        return this.pet;
    }

    @SuppressWarnings("unused")
    public TaskCompletionRecord getRecord() {
        return this.record;
    }
}
