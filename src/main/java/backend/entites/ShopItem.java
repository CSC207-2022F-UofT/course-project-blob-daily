package backend.entites;

import backend.entites.IDs.ItemID;

import java.sql.Blob;

public class ShopItem {
    //attributes are final since the charateristic of shopItem doesn't change
    private ItemID itemId;
    private final Double cost;
    private final String name;
    private final String description;
    private final Blob image;


    // Contructor
    public ShopItem(ItemID itemId, Double cost, String name, String description, Blob image){
        this.itemId = itemId;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.image = image;
    }


    // Getters
    public ItemID getItemId(){
        return itemId;
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
