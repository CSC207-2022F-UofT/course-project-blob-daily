package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.FriendController;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.users.DBAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendsManager {

    // helper functions
    public static ArrayList<String> getFriends(String userID) {
        System.out.println(userID);
        Optional<Friend> friendList = FriendController.friendsRepo.findById(userID);
        if(friendList.isPresent()) {
            return friendList.get().getFriends();
        }
        return new ArrayList<>();
    }

    public static boolean areFriends(String senderID, String receiverID) {
        Optional<Friend> sender = FriendController.friendsRepo.findById(senderID);
        Optional<Friend> receiver = FriendController.friendsRepo.findById(receiverID);
        if (sender.isPresent() && receiver.isPresent()) {
            return sender.get().getFriends().contains(receiverID) && receiver.get().getFriends().contains(senderID);
        }
        return false;
    }

    // Usecases
    public static ResponseEntity<Object> addFriend(String userName, String friendUsername) {
        try {
            DBAccount userAccount = AccountController.accountsRepo.findByUsername(userName);
            String userID = userAccount.getAccountID();
            DBAccount friendAccount = AccountController.accountsRepo.findByUsername(friendUsername);
            String friendID = friendAccount.getAccountID();

            if (FriendController.friendsRepo.existsById(userID)) {
                System.out.println("does it get to here baby?");
                ArrayList<String> friendsList = FriendsManager.getFriends(userID);
                if (friendsList.contains(friendID)) {
                    return new ResponseEntity<>("Friend already exists!", HttpStatus.CONFLICT);
                } else {
                    friendsList.add(friendID);
                    FriendController.friendsRepo.save(new Friend(userID, friendsList));

                }
            } else {
                ArrayList<String> friendsList = new ArrayList<>();
                friendsList.add(friendID);
                FriendController.friendsRepo.save(new Friend(userID, friendsList));
            }
            return new ResponseEntity<>("Friend successfully added! for both sides!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete friend
    public static ResponseEntity<Object> deleteFriend(String userName, String friendUsername, String sessionID) {
        try {
            String userID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
            String friendID = AccountController.accountsRepo.findByUsername(friendUsername).getAccountID();
            if (FriendController.friendsRepo.existsById(userID) && FriendController.friendsRepo.existsById(friendID)) {
                ArrayList<String> userList = FriendsManager.getFriends(userID);
                ArrayList<String> friendList = FriendsManager.getFriends(friendID);
                if (userList.size() == 0) {
                    return new ResponseEntity<>("Friend does not exists!", HttpStatus.CONFLICT);
                } else {
                    if (userList.contains(friendID)) {
                        userList.remove(friendID);
                        FriendController.friendsRepo.save(new Friend(userID, userList));
                    } else {
                        return new ResponseEntity<>("Friend does not exists! in Usere's List", HttpStatus.CONFLICT);
                    }
                }
                if (friendList.size() == 0) {
                    return new ResponseEntity<>("User does not exist in Friend's List!", HttpStatus.CONFLICT);
                } else {
                    if (friendList.contains(userID)) {
                        friendList.remove(userID);
                        FriendController.friendsRepo.save(new Friend(friendID, friendList));
                    }
                }
                return new ResponseEntity<>("Successfully removed users from each other's friendList!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User does not exist in your Friends List!", HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public static ResponseEntity<Object> deleteAllCorrelatedFriends(AccountID userID) {
        try {
            List<Friend> friends = FriendController.friendsRepo.findAllContainingUserID(userID.getID());

            System.out.println(friends.size());

            for (Friend friend: friends) {
                ArrayList<String> updatedList = friend.getFriends();
                updatedList.remove(userID);
                FriendController.friendsRepo.save(new Friend(friend.getAccountID(), updatedList));
            }
            return new ResponseEntity<>("This is what it returned", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
