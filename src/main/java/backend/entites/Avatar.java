package backend.entites;

import java.util.List;

public class Avatar {
    private AccountID accountID;
    private Double health;
    private List<ShopItem> inventory;
    private List<ShopItem> currentOutfit;


    // Contructor
    public Avatar(AccountID accountID, Double health, List<ShopItem> inventory, List<ShopItem> currentOutfit){
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
