package com.backend.entities;

import com.backend.entities.IDs.ItemID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representation of a ShopItem Entity (ID, health, balance, inventory, currentOutfit)
 */
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
    private final String imageLink;
    private final int location;

    // Contructor
    @PersistenceCreator
    public ShopItem(String ID, Double cost, String name, String description, String imageLink, int location){
        this.itemID = new ItemID(ID);
        this.ID = ID;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.imageLink = imageLink;
        this.location = location;
    }

    // Getters
    /**
     * @return the id of the entity
     */
    public String getID(){
        return itemID.getID();
    }

    /**
     * @return the cost of the entity
     */
    public Double getCost(){
        return cost;
    }

    /**
     * @return the name of the entity
     */
    public String getName(){
        return name;
    }

    /**
     * @return the description of the entity
     */
    public String getDescription(){
        return description;
    }

    /**
     * @return the link for the image
     */
    public String getImageLink(){
        return imageLink;
    }

    /**
     * @return the location
     */
    public int getLocation(){
        return location;
    }
}