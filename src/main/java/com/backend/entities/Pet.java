package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "PetsCollection")
public class Pet {
    @Transient
    private final AccountID accountID;
    @Id
    private final String ID;
    private final Double health;
    private final Double balance;
    private final List<ShopItem> inventory;
    private final List<ShopItem> currentOutfit;


    // Contructor
    public Pet(String ID, Double health, Double balance, List<ShopItem> inventory, List<ShopItem> currentOutfit){
        this.accountID = new AccountID(ID);
        this.ID = ID;
        this.health = health;
        this.balance = balance;
        this.inventory = inventory;
        this.currentOutfit = currentOutfit;
    }

    // Getters
    public String getID(){
        return ID;
    }



    public double getHealth(){
        return this.health;
    }

    public double getBalance() {
        return this.balance;
    }

    public List<ShopItem> getInventory(){
        return this.inventory;
    }

    public List<ShopItem> getCurrentOutfit(){
        return currentOutfit;
    }
}