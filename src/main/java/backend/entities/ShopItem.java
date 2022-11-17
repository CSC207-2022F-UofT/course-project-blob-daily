package backend.entities;

import backend.entities.IDs.ItemID;

import java.sql.Blob;

public class ShopItem {
    //attributes are final since the charateristic of shopItem doesn't change
    private final ItemID itemID;
    private final Double cost;
    private final String name;
    private final String description;
    private final Blob image;


    // Contructor
    public ShopItem(ItemID itemID, Double cost, String name, String description, Blob image){
        this.itemID = itemID;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.image = image;
    }


    // Getters
    public ItemID getItemId(){
        return itemID;
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

    public Blob getImage(){
        return image;
    }
}