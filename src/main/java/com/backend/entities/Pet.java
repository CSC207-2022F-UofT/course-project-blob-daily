package com.backend.entities;

import com.backend.entities.IDs.AccountID;

import java.util.List;

public class Pet {
    private final AccountID accountID;
    private final Double health;
    private final List<ShopItem> inventory;
    private final List<ShopItem> currentOutfit;


    // Contructor
    public Pet(AccountID accountID, Double health, List<ShopItem> inventory, List<ShopItem> currentOutfit){
        this.accountID = accountID;
        this.health = health;
        this.inventory = inventory;
        this.currentOutfit = currentOutfit;
    }

    // Getters
    public AccountID getAccountID(){
        return this.accountID;
    }

    public double getHealth(){
        return this.health;
    }

    public List<ShopItem> getInventory(){
        return this.inventory;
    }

    public List<ShopItem> getCurrentOutfit(){
        return currentOutfit;
    }
}
