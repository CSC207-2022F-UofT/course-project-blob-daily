package com.backend.usecases;

import com.backend.controller.PetController;
import com.backend.controller.ShopController;
import com.backend.entities.IDs.ItemID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopManager {

    public static void addShopItem(){

        ItemID ID = new ItemID(null);
        ID.generateID();
        ShopItem shopItem = new ShopItem(ID.getID(), 29.90, "brown boots", "a pair of boots that protects you from the snow");
        ShopController.shopRepo.save(shopItem);

    }

    public static void addPet(){
        ArrayList<ShopItem> curOutfit = new ArrayList<>();
        curOutfit.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet("9j(Gc5g)Id0G#Pt9s?De", 85.00, curOutfit, new ArrayList<>());
        PetController.petRepo.save(pet);
    }

    public static ArrayList<ShopItem> getShopItems() {
        return (ArrayList<ShopItem>) ShopController.shopRepo.findAll();
    }

    public static Optional<Pet> getPet(String sessionID){
        String curAccount = "9j(Gc5g)Id0G#Pt9s?De";
        Optional<Pet> pet = PetController.petRepo.findById(curAccount);
        if (pet == null){
            LogHandler.logError(new Exception("pet is null"));
        }
        return pet;
    }

    public static boolean updateCurrentOutfit(String sessionID, List<String> newOutfit){
        throw new UnsupportedOperationException();
    }

    public static double getBalance(String sessionID){
        double balance = 28.9; //dataBase.getPet;

        return balance;
    }

    public static boolean purchaseItem(String itemID, String sessionID) {
        throw new UnsupportedOperationException();
    }
}
