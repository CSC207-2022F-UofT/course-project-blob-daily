package com.backend.usecases.managers;

import com.backend.entities.Friend;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.FriendsRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Configurable
public class FriendsManager {
    private static FriendsRepo friendsRepo;

    public FriendsManager(FriendsRepo friendsRepo) {
        FriendsManager.friendsRepo = friendsRepo;
    }
    // helper functions

    public ArrayList<String> getFriends(String userID) {
        Optional<Friend> friendList = friendsRepo.findById(userID);
        if (friendList.isPresent()) {
            return friendList.get().getFriends();
        }
        return new ArrayList<>();
    }

    public boolean areFriends(String senderID, String receiverID) {
        Optional<Friend> sender = friendsRepo.findById(senderID);
        Optional<Friend> receiver = friendsRepo.findById(receiverID);
        if (sender.isPresent() && receiver.isPresent()) {
            return sender.get().getFriends().contains(receiverID) && receiver.get().getFriends().contains(senderID);
        }
        return false;
    }

    // Use-cases
    public ResponseEntity<Object> addFriend(String userID, String friendUserID) {

        if (friendsRepo.existsById(userID)) {
            ArrayList<String> friendsList = new ArrayList<>();
            if (friendsRepo.findById(userID).isPresent()) {
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

    public boolean userExists(String userID) {
        return friendsRepo.existsById(userID);
    }

    public void updateFriendsList(Friend friendObject) {
        friendsRepo.save(friendObject);
    }

    public void deleteFriendById(String accountID) {
        friendsRepo.deleteById(accountID);
    }

    public List<Friend> getAllContainingUserID(String accountID) {
        return friendsRepo.findAllContainingUserID(accountID);
    }

}
