package com.backend.entities;

import com.backend.entities.IDs.AccountID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


/**
 * Representation of a Pet Entity (ID, health, balance, inventory, currentOutfit)
 */
@Document(collection = "PetsCollection")
public class Pet {
    @Transient
    private final AccountID accountID;
    @Id
    private final String ID;
    private final Double health;
    private final Double balance;
    private final ArrayList<ShopItem> inventory;
    private final ArrayList<ShopItem> currentOutfit;

    // Contructor
    public Pet(String ID, Double health, Double balance, ArrayList<ShopItem> inventory, ArrayList<ShopItem> currentOutfit){
        this.accountID = new AccountID(ID);
        this.ID = ID;
        this.health = health;
        this.balance = balance;
        this.inventory = inventory;
        this.currentOutfit = currentOutfit;
    }

    // Getters
    /**
     * @return the id of the entity
     */
    public String getID(){
        return ID;
    }

    /**
     * @return the health of the entity
     */
    public double getHealth(){
        return this.health;
    }

    /**
     * @return the balance of the entity
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * @return the inventory of the entity
     */
    public ArrayList<ShopItem> getInventory(){
        return this.inventory;
    }

    /**
     * @return the currentOutfit of the entity
     */
    public ArrayList<ShopItem> getCurrentOutfit(){
        return currentOutfit;
    }
}