package backend.useCases;

import backend.entities.IDs.AccountID;
import backend.entities.IDs.ItemID;
import backend.entities.Parser;
import backend.entities.Pet;
import backend.entities.ShopItem;
import backend.error.handlers.LogHandler;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Stream;


public class shopManager implements Parser {

//    Not yet implemented verify session

    public static HashMap convert(String stringObject){
        HashMap<String, Object> hashObject = new HashMap<>();
        int keyIndex = 1;
        int colonIndex = stringObject.indexOf(":");
        int commaIndex = stringObject.indexOf(",");

        //If comma doesn't exist in the stringObject, set it to be last index
        if(commaIndex == -1){
            commaIndex = stringObject.length() - 1;
        }

        int listIndex = stringObject.indexOf("[");


        while (stringObject.length() > 1){
            String key = stringObject.substring(keyIndex, colonIndex);

            // If "[" exist in stringObject, then there is a list in the hashmap

            String value = stringObject.substring(colonIndex + 2, commaIndex);
            hashObject.put(key, value);



            // updating stringObject to not included the previously added key-value pair
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


    public static List<ShopItem> getShopItems() {
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        ArrayList<String> itemList = new ArrayList<>(List.of("{itemID: 12jadf2, cost: 13.2, name: pants, description: it's very comfortable, image: abc}")); //dataBase.getShopItems;
        for (String s : itemList) {
            HashMap hashObject = convert(s);
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

    public static Pet getPet(String sessionID){
        String petString = "{accountID: 12jadf2, health: 13.2, inventory: [pants], currentOutfit: [pants]}"; //dataBase.getPet;
        HashMap petHash = convert(petString);

        AccountID accountID = new AccountID((String) petHash.get("accountID"));
        Double health = Double.parseDouble((String) petHash.get("health"));
        ArrayList<ShopItem> inventory = (ArrayList) petHash.get("inventory");
        ArrayList<ShopItem> currentOutfit = (ArrayList) petHash.get("currentOutfit");

        Pet pet = new Pet(accountID, health, inventory, currentOutfit);

        return pet;
    }

    public static boolean updateCurrentOutfit(String sessionID, List<String> newOutfit){
        throw new UnsupportedOperationException();
    }

    public static double getBalance(String sessionID){
        String balanceString = "{balance: 28.9}"; //dataBase.getPet;
        HashMap balanceHash = convert(balanceString);
        return Double.parseDouble((String) balanceHash.get("balance"));
    }

    public static boolean purchaseItem(String itemID, String sessionID) {
        throw new UnsupportedOperationException();
    }

}
