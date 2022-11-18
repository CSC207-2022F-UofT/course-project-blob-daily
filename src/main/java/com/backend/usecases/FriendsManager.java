package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.FriendController;
import com.backend.entities.Friend;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class FriendsManager {

    public static ArrayList<String> getFriends(String userID) {
        return FriendController.friendsRepo.getFriendsList(userID);
    }

    // add friend
    public static ResponseEntity<Object> addFriend(String userName, String friendUsername) {
        try {
            String accountID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
            String friendID = AccountController.accountsRepo.findByUsername(friendUsername).getAccountID();
            if(FriendController.friendsRepo.existsById(accountID)) {
                ArrayList<String> friendsList = FriendsManager.getFriends(accountID);
                if(friendsList.size() == 0) {
                    friendsList.add(friendID);
                    FriendController.friendsRepo.save(new Friend(accountID, friendsList));
                    return new ResponseEntity<>("Friend successfully added!", HttpStatus.OK);
                }
                else {
                    if(friendsList.contains(friendUsername)) {
                        return new ResponseEntity<>("Friend already exists!", HttpStatus.CONFLICT);
                    }
                    else {
                        friendsList.add(friendUsername);
                        FriendController.friendsRepo.save(new Friend(accountID, friendsList));
                        return new ResponseEntity<>("Friend successfully added!", HttpStatus.OK);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete friend
    public static ResponseEntity<Object> deleteFriend(String userName, String friendUsername) {
        try {
            String accountID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
            if(FriendController.friendsRepo.existsById(accountID)) {
                ArrayList<String> friendsList = FriendsManager.getFriends(accountID);
                if(friendsList.size() == 0) {
                    return new ResponseEntity<>("Friend does not exists!", HttpStatus.CONFLICT);
                }
                else {
                    if(friendsList.contains(friendUsername)) {
                        friendsList.remove(friendUsername);
                        FriendController.friendsRepo.save(new Friend(accountID, friendsList));
                        return new ResponseEntity<>("Friend successfully deleted!", HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>("Friend does not exists!", HttpStatus.CONFLICT);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}
