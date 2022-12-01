package com.backend.usecases.managers;

import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.ProtectedAccount;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.FriendsRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Configurable
public class FriendsManager {
    private static FriendsRepo friendsRepo;
    public FriendsManager(FriendsRepo friendsRepo) {
        FriendsManager.friendsRepo = friendsRepo;
    }
    // helper functions
    public ArrayList<> getUserFriends(String sessionID) {

        AccountID userID = AccountManager.verifySession(new SessionID(sessionID));
        if (userID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Optional<Friend> friendIDs = friendsRepo.findById(userID.getID());

        if(friendIDs.isPresent()) {
            ArrayList<String> tempFriends = friendIDs.get().getFriends();
            ArrayList<String> friendList = new ArrayList<>();
            for(String friendID: tempFriends) {
                String friendUsername = ((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(new AccountID(friendID)).getBody())).getUsername();
                friendList.add(friendUsername);
            }
            return new ResponseEntity<>(friendList, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    private ArrayList<String> getFriends(String userID) {
        Optional<Friend> friendList = friendsRepo.findById(userID);
        if (friendList.isPresent()) {
            return friendList.get().getFriends();
        }
        return new ArrayList<>();
    }

    public static boolean areFriends(String senderID, String receiverID) {
        Optional<Friend> sender = friendsRepo.findById(senderID);
        Optional<Friend> receiver = friendsRepo.findById(receiverID);
        if (sender.isPresent() && receiver.isPresent()) {
            return sender.get().getFriends().contains(receiverID) && receiver.get().getFriends().contains(senderID);
        }
        return false;
    }

    // Use cases
    public static ResponseEntity<Object> addFriend(String userID, String friendUserID) {

        if (friendsRepo.existsById(userID)) {
            ArrayList<String> friendsList = new ArrayList<>();
            if(friendsRepo.findById(userID).isPresent()) {
                friendsList = friendsRepo.findById(userID).get().getFriends();
            }

            if (friendsList.contains(friendUserID)) {
                return LogHandler.logError(new FileAlreadyExistsException("Friend already exists!"), HttpStatus.CONFLICT);
            } else {
                friendsList.add(friendUserID);
                friendsRepo.save(new Friend(userID, friendsList));
            }
        } else {
            ArrayList<String> friendsList = new ArrayList<>();
            friendsList.add(friendUserID);
            friendsRepo.save(new Friend(userID, friendsList));
        }
        return new ResponseEntity<>("Friend successfully added! for both sides!", HttpStatus.OK);
    }

    // delete friend
    public static ResponseEntity<Object> deleteFriend(String friendUsername, String sessionID) {

        // Check if valid session
        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Find each friend's accountID
        String userID = accountID.getID();
        String friendID = Objects.requireNonNull(AccountManager.getAccountIDByUsername(friendUsername)).getID();

        if (friendsRepo.existsById(userID) && friendsRepo.existsById(friendID)) {

            // retrieve user and friend's friendList
            ArrayList<String> userList = FriendsManager.getFriends(userID);
            ArrayList<String> friendList = FriendsManager.getFriends(friendID);

            // if the user does not have any friends, return LogHandler message
            if (userList.size() == 0) {
                return LogHandler.logError(new NoSuchFileException("Friend does not exist!"), HttpStatus.NOT_FOUND);
            } else {
                if (userList.contains(friendID)) {
                    userList.remove(friendID);
                    friendsRepo.save(new Friend(userID, userList));
                } else {
                    return LogHandler.logError(new NoSuchFileException("Friend does not exist in user's list!"), HttpStatus.NOT_FOUND);
                }
            }
            if (friendList.size() == 0) {
                return LogHandler.logError(new NoSuchFileException("Friend does not exist!"), HttpStatus.NOT_FOUND);
            } else {
                if (friendList.contains(userID)) {
                    friendList.remove(userID);
                    friendsRepo.save(new Friend(friendID, friendList));
                }
            }
            return new ResponseEntity<>("Successfully removed users from each other's friendList!", HttpStatus.OK);
        } else {
            return LogHandler.logError(new NoSuchFileException("Both do not exist in each other's list!"), HttpStatus.NOT_FOUND);
        }
    }

    // used by deleteAccount in AccountManager
    public static ResponseEntity<Object> deleteAllCorrelatedFriends(String sessionID) {
        // Check if valid session
        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Delete the user in Friends DB
        friendsRepo.deleteById(accountID.getID());

        List<Friend> friends = friendsRepo.findAllContainingUserID(accountID.getID());

        System.out.println(friends.size());

        // Delete instance of user in each of its friend's list
        for (Friend friend : friends) {
            ArrayList<String> updatedList = friend.getFriends();
            updatedList.remove(accountID.getID());
            friendsRepo.save(new Friend(friend.getAccountID(), updatedList));
        }
        return new ResponseEntity<>("Delete user for all Correlated Friends list!", HttpStatus.OK);
    }

}
