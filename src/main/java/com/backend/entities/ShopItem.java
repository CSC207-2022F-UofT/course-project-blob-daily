package com.backend.entities;

import com.backend.entities.IDs.ItemID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ShopItemsCollection")
public class ShopItem {
    //attributes are final since the characteristic of shopItem doesn't change
    @Transient
    private final ItemID itemID;
    @Id
    private final String ID;
    private final Double cost;
    private final String name;
    private final String description;



    // Contructor
    @PersistenceCreator
    public ShopItem(String ID, Double cost, String name, String description){
        this.itemID = new ItemID(ID);
        this.ID = ID;
        this.cost = cost;
        this.name = name;
        this.description = description;
    }


    // Getters
    public String getID(){
        return itemID.getID();
    }

    public Double getCost(){
        return cost;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

}