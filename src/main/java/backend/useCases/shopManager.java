package backend.useCases;

import backend.entities.IDs.AccountID;
import backend.entities.IDs.ItemID;
import backend.entities.Pet;
import backend.entities.ShopItem;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class shopManager {

//    Not yet implemented verify session


    public HashMap hashmapConverter(String stringObject){
        HashMap<String, Object> hashObject = new HashMap<>();
        int keyIndex = 1;
        int colonIndex = stringObject.indexOf(":");
        int commaIndex = stringObject.indexOf(",");
        if(commaIndex == -1){
            commaIndex = stringObject.length() - 1;
        }
        int listIndex = stringObject.indexOf("[");
        while (stringObject.length() > 1){
            String key = stringObject.substring(keyIndex, colonIndex);
            System.out.print(key);
            if(listIndex != -1 && listIndex < commaIndex){
                ArrayList<String> value = new ArrayList<>();
                hashObject.put(key, value);
            }
            else {
                String value = stringObject.substring(colonIndex + 2, commaIndex);
                hashObject.put(key, value);
                System.out.println(value);
            }
            if(commaIndex + 2 > stringObject.length()){
                stringObject = "";
            }else{
                stringObject = stringObject.substring(commaIndex + 2);
            }

            keyIndex = 0;
            colonIndex = stringObject.indexOf(":");
            commaIndex = stringObject.indexOf(",");
            if(commaIndex == -1){
                commaIndex = stringObject.length() - 1;
            }
        }
        return hashObject;
    }


    public List<ShopItem> getShopItems(){
        ArrayList<ShopItem> shopItems= new ArrayList<>();
        ArrayList<String> itemList= new ArrayList<>(List.of("{itemID: 12jadf2, cost: 13.2, name: pants, description: it's very comfortable, image: abc}")); //dataBase.getShopItems;
        for (String s : itemList) {
            HashMap hashObject = hashmapConverter(s);
            String itemIDString = (String) hashObject.get("itemID");
            Double cost = Double.valueOf((String) hashObject.get("cost"));
            String name = (String) hashObject.get("name");
            String description = (String) hashObject.get("description");
            String imageString = (String) hashObject.get("image");


            ItemID itemID = new ItemID(itemIDString);



            shopItems.add(new ShopItem(itemID, cost, name, description, imageString));
        }
        return shopItems;

    }

    public Pet getPet(String sessionID){
        String petString = "{accountID: 12jadf2, health: 13.2, inventory: [pants], currentOutfit: [pants]}"; //dataBase.getPet;
        HashMap petHash = hashmapConverter(petString);

        AccountID accountID = new AccountID((String) petHash.get("accountID"));
        Double health = (Double) petHash.get("health");
        ArrayList<ShopItem> inventory = (ArrayList<ShopItem>) petHash.get("inventory");
        ArrayList<ShopItem> currentOutfit = (ArrayList<ShopItem>) petHash.get("currentOutfit");

        Pet pet = new Pet(accountID, health, inventory, currentOutfit);

        return pet;
    }

    public boolean updateCurrentOutfit(String sessionID, List<String> newOutfit){
        throw new UnsupportedOperationException();
    }

    public double getBalance(String sessionID){
        String balanceString = "{balance: 28.9}"; //dataBase.getPet;
        HashMap balanceHash = hashmapConverter(balanceString);
        Double balance = (Double) balanceHash.get("balance");
        return balance;
    }

    public boolean purchaseItem(String itemID, String sessionID) {
        throw new UnsupportedOperationException();
    }
}
