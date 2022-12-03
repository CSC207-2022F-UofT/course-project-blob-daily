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

    /**
     * Get the ProtectedAccount information associated with this feedItem
     * @return the ProtectedAccount for this instance
     */
    public ProtectedAccount getAccount() {
        return this.account;
    }

    /**
     * Get the Pet information associated with this feedItem
     * @return the Pet for this instance
     */
    public Pet getPet() {
        return this.pet;
    }

    /**
     * Get the TaskCompletionRecord information associated with this feedItem
     * @return the TaskCompletionRecord for this instance
     */
    @SuppressWarnings("unused")
    public TaskCompletionRecord getRecord() {
        return this.record;
    }
}
