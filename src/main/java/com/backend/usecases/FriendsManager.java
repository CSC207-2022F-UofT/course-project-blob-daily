package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.FriendController;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FriendsManager {

    // helper functions
    public static ResponseEntity<Object> getFriends(String userName, String sessionID) {

        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (!((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(accountID).getBody())).getUsername().equals(userName)) {
            return LogHandler.logError(new SessionException("Invalid session!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AccountID userID = AccountManager.getAccountIDByUsername(userName);
        assert userID != null;
        Optional<Friend> friendList = FriendController.friendsRepo.findById(userID.getID());
        if (friendList.isPresent()) {
            return new ResponseEntity<>(friendList.get().getFriends(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    public static ArrayList<String> getFriends(String userName) {
        AccountID userID = AccountManager.getAccountIDByUsername(userName);
        assert userID != null;
        Optional<Friend> friendList = FriendController.friendsRepo.findById(userID.getID());
        if (friendList.isPresent()) {
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

    // Use cases
    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> addFriend(String userName, String friendUsername, String sessionID) {
        Account userAccount = AccountController.accountsRepo.findByUsername(userName);
        String userID = userAccount.getAccountID();
        Account friendAccount = AccountController.accountsRepo.findByUsername(friendUsername);
        String friendID = friendAccount.getAccountID();

        if (FriendController.friendsRepo.existsById(userID)) {
            System.out.println("does it get to here baby?");
            ResponseEntity<Object> verification = FriendsManager.getFriends(userID, sessionID);
            if (!(verification.getBody() instanceof ArrayList)) return verification;

            ArrayList<String> friendsList = (ArrayList<String>) verification.getBody();

            if (friendsList.contains(friendID)) {
                return LogHandler.logError(new FileAlreadyExistsException("Friend already exists!"), HttpStatus.CONFLICT);
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
    }

    // Overloading method
    public static ResponseEntity<Object> addFriend(String userName, String friendUsername) {
        Account userAccount = AccountController.accountsRepo.findByUsername(userName);
        String userID = userAccount.getAccountID();
        Account friendAccount = AccountController.accountsRepo.findByUsername(friendUsername);
        String friendID = friendAccount.getAccountID();

        if (FriendController.friendsRepo.existsById(userID)) {

            ArrayList<String> friendsList = FriendsManager.getFriends(userID);

            if (friendsList.contains(friendID)) {
                return LogHandler.logError(new FileAlreadyExistsException("Friend already exists!"), HttpStatus.CONFLICT);
            } else {
                friendsList.add(friendID);
                FriendController.friendsRepo.save(new Friend(userID, friendsList));

            }
        } else {
            ArrayList<String> friendsList = new ArrayList<>();
            friendsList.add(friendID);
            FriendController.friendsRepo.save(new Friend(userID, friendsList));
        }
        return new ResponseEntity<>("Friend successfully added for both sides!", HttpStatus.OK);
    }

    // delete friend
    public static ResponseEntity<Object> deleteFriend(String userName, String friendUsername, String sessionID) {

        // Check if valid session
        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(accountID).getBody())).getUsername().equals(userName)) {
            return LogHandler.logError(new SessionException("Invalid session!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Find each user and friend's accountID
        String userID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
        String friendID = AccountController.accountsRepo.findByUsername(friendUsername).getAccountID();

        if (FriendController.friendsRepo.existsById(userID) && FriendController.friendsRepo.existsById(friendID)) {

            // retrieve user and friend's friendList
            ArrayList<String> userList = FriendsManager.getFriends(userID);
            ArrayList<String> friendList = FriendsManager.getFriends(friendID);

            // if the user does not have any friends, return LogHandler message
            if (userList.size() == 0) {
                return LogHandler.logError(new NoSuchFileException("Friend does not exist!"), HttpStatus.NOT_FOUND);
            } else {
                if (userList.contains(friendID)) {
                    userList.remove(friendID);
                    FriendController.friendsRepo.save(new Friend(userID, userList));
                } else {
                    return LogHandler.logError(new NoSuchFileException("Friend does not exist in user's list!"), HttpStatus.NOT_FOUND);
                }
            }
            if (friendList.size() == 0) {
                return LogHandler.logError(new NoSuchFileException("Friend does not exist!"), HttpStatus.NOT_FOUND);
            } else {
                if (friendList.contains(userID)) {
                    friendList.remove(userID);
                    FriendController.friendsRepo.save(new Friend(friendID, friendList));
                }
            }
            return new ResponseEntity<>("Successfully removed users from each other's friendList!", HttpStatus.OK);
        } else {
            return LogHandler.logError(new NoSuchFileException("Both do not exist in each other's list!"), HttpStatus.NOT_FOUND);
        }
    }


    public static ResponseEntity<Object> deleteAllCorrelatedFriends(String userName, String sessionID) {

        // Check if valid session
        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(accountID).getBody())).getUsername().equals(userName)) {
            return LogHandler.logError(new SessionException("Invalid session!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Delete the user in Friends DB
        FriendController.friendsRepo.deleteById(accountID.getID());

        List<Friend> friends = FriendController.friendsRepo.findAllContainingUserID(accountID.getID());

        System.out.println(friends.size());

        // Delete instance of user in each of its friend's list
        for (Friend friend : friends) {
            ArrayList<String> updatedList = friend.getFriends();
            updatedList.remove(accountID.getID());
            FriendController.friendsRepo.save(new Friend(friend.getAccountID(), updatedList));
        }
        return new ResponseEntity<>("This is what it returned", HttpStatus.OK);
    }

}