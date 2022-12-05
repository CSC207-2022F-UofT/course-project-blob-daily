package com.backend.usecases.managers;

import com.backend.entities.Friend;
import com.backend.repositories.FriendsRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final FriendsRepo friendsRepo;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the Friends Repository and ErrorHandler
     * @param friendsRepo repository to be injected
     * @param errorHandler the error handler to be injected
     */
    @SuppressWarnings("unused")
    @Autowired
    public FriendsManager(FriendsRepo friendsRepo, IErrorHandler errorHandler) {
        this.friendsRepo = friendsRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Retrieve a list of friends
     * @param userID of type String to reference corresponding list of friends
     * @return an ArrayList user's friends as AccountID, Otherwise return an
     * empty list
     */
    // Use-cases
    public ArrayList<String> getFriends(String userID) {
        Optional<Friend> friendList = friendsRepo.findById(userID);
        if (friendList.isPresent()) {
            return friendList.get().getFriends();
        }
        return new ArrayList<>();
    }

    /**
     * Checks if the two users are friends of each other
     * @param senderID of type String, reference to the user as Sender
     * @param receiverID of type String, reference to the user as Receiver
     * @return the truth value of whether the two users are friends or not
     */
    public boolean areFriends(String senderID, String receiverID) {
        Optional<Friend> sender = friendsRepo.findById(senderID);
        Optional<Friend> receiver = friendsRepo.findById(receiverID);
        if (sender.isPresent() && receiver.isPresent()) {
            return sender.get().getFriends().contains(receiverID) && receiver.get().getFriends().contains(senderID);
        }
        return false;
    }

    /**
     * Add friendUser as friend to User
     * @param userID of type String, reference to the current User
     * @param friendUserID of type String, reference to the friend to be added
     * @return ResponseEntity Object detailing the success of adding friendUser as friend of the User
     * or errors
     */
    public ResponseEntity<Object> addFriend(String userID, String friendUserID) {

        if (friendsRepo.existsById(userID)) {
            ArrayList<String> friendsList = new ArrayList<>();
            if (friendsRepo.findById(userID).isPresent()) {
                friendsList = friendsRepo.findById(userID).get().getFriends();
            }

            if (friendsList.contains(friendUserID)) {
                return this.errorHandler.logError(new FileAlreadyExistsException("Friend already exists!"), HttpStatus.CONFLICT);
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

    // Repo specific methods

    /**
     * Checks if userExists within Friends Collection
     * @param userID of type String, userID to reference corresponding account to retrieve
     * @return a boolean whether user Exists in Friends Collection or not
     */
    public boolean userExists(String userID) {
        return friendsRepo.existsById(userID);
    }

    /**
     * Updates the friend Object
     * @param friendObject of type Friend, the object to be updated
     */
    public void updateFriendsList(Friend friendObject) {
        friendsRepo.save(friendObject);
    }

    /**
     * deletes the user's friend data
     * @param accountID of type String, accountID to reference corresponding account to delete from
     */
    public void deleteFriendByID(String accountID) {
        friendsRepo.deleteById(accountID);
    }

    /**
     * Find all accounts containing userID in their friends list
     * @param accountID of type String, accountID to reference corresponding account
     * @return a List of type Friends, List of Friends that all contain user as friend
     */
    public List<Friend> getAllContainingUserID(String accountID) {
        return friendsRepo.findAllContainingUserID(accountID);
    }

}
