package com.backend.usecases.facades;

import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.entities.users.ProtectedAccount;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.FriendsManager;
import com.backend.usecases.managers.InvitationsManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class FriendSystemFacade {
    private final AccountManager accountManager;
    private final InvitationsManager invitationsManager;
    private final FriendsManager friendsManager;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of accountManger, invitationsManger, friendsManager, and errorHandler
     * @param accountManager is AccountManager to be injected
     * @param invitationsManager is InvitationsManager to be injected
     * @param friendsManager is FriendsManager to be injected
     * @param errorHandler is ErrorHandler to be injected
     */
    @SuppressWarnings("unused")
    @Autowired
    public FriendSystemFacade(AccountManager accountManager, InvitationsManager invitationsManager, FriendsManager friendsManager, IErrorHandler errorHandler) {
        this.accountManager = accountManager;
        this.invitationsManager = invitationsManager;
        this.friendsManager = friendsManager;
        this.errorHandler = errorHandler;
    }

    /**
     * A Helper function to verify both Session and Invitation
     * @param receiverUsername of type String, username to reference and verify both session and invitation
     * @param sessionID of type String, a sessionID to reference and verify both session and invitation
     * @return a response entity detailing successful verification of session and invitation, or any associated error
     */
    public ResponseEntity<Object> verifySessionAndInvitation(String receiverUsername, String sessionID) {
        // check if it is a valid session
        AccountID userID = this.accountManager.verifySession(new SessionID(sessionID));
        if (userID == null)
            return this.errorHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);

        // receiver senderUsername
        String senderUsername = this.accountManager.getAccountInfo(userID).getUsername();

        // verify valid invitation. If successful, receive senderID and accountID
        AccountID senderID = this.accountManager.getAccountIDByUsername(senderUsername);
        AccountID receiverID = this.accountManager.getAccountIDByUsername(receiverUsername);
        ResponseEntity<Object> verification = this.invitationsManager.verifyInvitation(new Invitation(senderID, receiverID, new Date(System.currentTimeMillis())));
        if (!(verification.getBody() instanceof ArrayList)) return verification;
        ArrayList<AccountID> result = new ArrayList<>();
        result.add(senderID);
        result.add(receiverID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves the friends of the user
     * @param sessionID of type String, sessionID to reference corresponding account to find the user's friends
     * @return a response entity of an array of protectedAccount that are the friends of the user in JSON format or any
     * associated error
     */
    public ResponseEntity<Object> getUserFriends(String sessionID) {
        AccountID userID = this.accountManager.verifySession(new SessionID(sessionID));
        if (userID == null) {
            return this.errorHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        JSONObject invites = new JSONObject();
        ArrayList<String> friendIDs = this.friendsManager.getFriends(userID.getID());
        JSONArray friends = new JSONArray();

        if (friendIDs.size() != 0) {
            for (String friendID : friendIDs) {
                ProtectedAccount friendAccount = this.accountManager.getAccountInfo(new AccountID(friendID));
                friends.add(friendAccount);
            }
        }
        invites.put("friends", friends);
        return new ResponseEntity<>(invites, HttpStatus.OK);
    }

    /**
     * Deletion of a friend (remove from DB) given a valid sessionID and friendUsername
     * @param friendUsername of type String, a username to reference and delete the friend from the user's friend list
     * @param sessionID of type String, sessionID to reference corresponding account to delete friend from
     * @return a response entity detailing successful deletion of friend or any associated error
     */
    public ResponseEntity<Object> deleteFriend(String friendUsername, String sessionID) {
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return this.errorHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Find each friend's accountID
        String userID = accountID.getID();
        String friendID = Objects.requireNonNull(this.accountManager.getAccountIDByUsername(friendUsername)).getID();

        if (this.friendsManager.userExists(userID) && this.friendsManager.userExists(friendID)) {
            ArrayList<String> userList = this.friendsManager.getFriends(userID);
            ArrayList<String> friendList = this.friendsManager.getFriends(friendID);

            if (userList.size() == 0) {
                return this.errorHandler.logError(new NoSuchFileException("User's friend list is empty!"), HttpStatus.NOT_FOUND);
            } else {
                if (userList.contains(friendID)) {
                    userList.remove(friendID);
                    this.friendsManager.updateFriendsList(new Friend(new AccountID(userID), userList));
                } else {
                    return this.errorHandler.logError(new NoSuchFileException("Friend does not exist in user's list!"), HttpStatus.NOT_FOUND);
                }
            }
            if (friendList.size() == 0) {
                return this.errorHandler.logError(new NoSuchFileException("User does not exist in Friend's List!"), HttpStatus.NOT_FOUND);
            } else {
                if (friendList.contains(userID)) {
                    friendList.remove(userID);
                    this.friendsManager.updateFriendsList(new Friend(friendID, friendList));
                }
            }
            return new ResponseEntity<>("Successfully removed users from each other's friendList!", HttpStatus.OK);

        } else if (!this.friendsManager.userExists(userID) && !this.friendsManager.userExists(friendID))
            return this.errorHandler.logError(new NoSuchFileException("Both do not have Friends!"), HttpStatus.NOT_FOUND);
        else if (!this.friendsManager.userExists(userID))
            return this.errorHandler.logError(new NoSuchFileException("User does not have friends!"), HttpStatus.NOT_FOUND);
        return this.errorHandler.logError(new NoSuchFileException("Friend does not have friends!"), HttpStatus.NOT_FOUND);
    }

    /**
     * Deletion of user's instance in all friends list that contains user (specific for Account Deletion)
     * @param sessionID of type String, sessionID to reference corresponding account to delete from all friends list
     * @return a response entity detailing successful deletion of user from every associated friends list or any
     * associated error
     */
    // used by deleteAccount in AccountManager
    public ResponseEntity<Object> deleteAllCorrelatedFriends(String sessionID) {
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return this.errorHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Delete the user in FriendsDB
        this.friendsManager.deleteFriendByID(accountID.getID());

        List<Friend> friends = this.friendsManager.getAllContainingUserID(accountID.getID());

        for (Friend friend : friends) {
            ArrayList<String> updatedList = friend.getFriends();
            updatedList.remove(accountID.getID());
            this.friendsManager.updateFriendsList(new Friend(friend.getAccountID(), updatedList));
        }
        return new ResponseEntity<>("Deleted user for all Correlated Friends list!", HttpStatus.OK);
    }

    /**
     * Retrieve invitations of the user as Receiver or Sender
     * @param sessionID of type String, sessionID to reference corresponding account to retrieve invitations
     * @param isReceiver of type boolean, informs the function to retrieve invitations as sender or receiver
     * @return a response entity detailing successful retrieval of invites, as well as a JSONObject of invitations as
     * Array. Otherwise, returns any associated error
     */
    public ResponseEntity<Object> getInvitations(String sessionID, boolean isReceiver) {
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return this.errorHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        JSONObject invites = new JSONObject();
        List<Invitation> accounts;

        if (isReceiver) accounts = this.invitationsManager.getAllByReceiverID(accountID.getID());
        else accounts = this.invitationsManager.getAllBySenderID(accountID.getID());

        JSONArray users = new JSONArray();

        for (Invitation account : accounts) {
            ProtectedAccount friendAccount = this.accountManager.getAccountInfo(new AccountID((isReceiver ? account.getSenderID(): account.getReceiverID())));
            users.add(friendAccount);
        }

        invites.put("invites", users);

        return new ResponseEntity<>(invites, HttpStatus.OK);
    }

    /**
     * Creation of Invitation instance with given parameters
     * @param receiverUsername of type String, username to reference the user who is receiving the Invitation
     * @param sessionID of type String, sessionID to reference corresponding sender account to create Invitation
     * @return a response entity detailing successful creation of Invitation or any associated error
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> sendInvitation(String receiverUsername, String sessionID) {
        // check if it is a valid session
        ResponseEntity<Object> result = verifySessionAndInvitation(receiverUsername, sessionID);
        if(!(result.getBody() instanceof ArrayList)) return result;
        AccountID receiverID = ((ArrayList<AccountID>) result.getBody()).get(1);
        AccountID senderID = ((ArrayList<AccountID>) result.getBody()).get(0);



        // Check if invitation already exists
        if (this.invitationsManager.invitationExists(senderID.getID(), receiverID.getID())) {
            return this.errorHandler.logError(new FileAlreadyExistsException("Invitation already sent!"), HttpStatus.PRECONDITION_FAILED);
        }

        // check if other user already sent invitation
        if (this.invitationsManager.invitationExists(receiverID.getID(), senderID.getID())) {
            // if then, automatically accept each other
            handleInvitation(receiverUsername, sessionID, true);
            return new ResponseEntity<>("Friend request automatically accepted!", HttpStatus.OK);
        }

        if (this.friendsManager.areFriends(senderID.getID(), receiverID.getID())) {
            return this.errorHandler.logError(new FileAlreadyExistsException("Users are already friends!"), HttpStatus.PRECONDITION_FAILED);
        }

        // Create invitation
        Invitation invitation = new Invitation(senderID.getID() + receiverID.getID(), senderID.getID(), receiverID.getID(), new Date(System.currentTimeMillis()));

        // Save it to DB
        this.invitationsManager.saveInvitation(invitation);
        return new ResponseEntity<>("Invitation successfully sent!", HttpStatus.CREATED);
    }

    /**
     * Delete Invitation as sender
     * @param receiverUsername as type of String, a reference to corresponding user that is receiving the Invitation
     * @param sessionID as type of String, a sessionID to reference
     * @return a response entity detailing successful withdrawal or any associated error
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> withdrawInvitation(String receiverUsername, String sessionID) {
        // check if it is a valid session
        ResponseEntity<Object> result = verifySessionAndInvitation(receiverUsername, sessionID);
        AccountID senderID;
        AccountID receiverID;
        if (!(result.getBody() instanceof ArrayList)) return result;
        else {
            senderID = ((ArrayList<AccountID>) result.getBody()).get(0);
            receiverID = ((ArrayList<AccountID>) result.getBody()).get(1);
        }


        // Check if sender and receiver are not the same
        if (senderID.getID().equals(receiverID.getID())) {
            return this.errorHandler.logError(new InvalidParameterException("Sender and Receiver are the same!"), HttpStatus.PRECONDITION_FAILED);
        }

        // Check invitation exists in DB
        if (this.invitationsManager.invitationExists(senderID.getID(), receiverID.getID())) {
            // delete invitation
            return this.invitationsManager.deleteInvitation(receiverID.getID(), senderID.getID());
        } else {
            return this.errorHandler.logError(new Exception("Invitation does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Accepts or Declines Invitation associated with the given username and sessionID
     * @param receiverUsername of type String, a reference to corresponding user
     * @param sessionID of type String, the identifier used reference the sender account information
     * @param isAccepted of type boolean, the identifier used to determine if invitation is accepted or not
     * @return a response entity detailing successful handling of invitation or any associated error
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> handleInvitation(String receiverUsername, String sessionID, boolean isAccepted) {
        // check if it is a valid session
        ResponseEntity<Object> result = verifySessionAndInvitation(receiverUsername, sessionID);
        if(!(result.getBody() instanceof ArrayList)) return result;
        AccountID senderID = ((ArrayList<AccountID>) result.getBody()).get(0);
        AccountID receiverID = ((ArrayList<AccountID>) result.getBody()).get(1);

        // Otherwise, update each other's friend's list and delete the existing invitation
        // and check if the other user already sent invitation
        if (isAccepted) {
            // addFriend
            ResponseEntity<Object> firstCheck = this.friendsManager.addFriend(senderID.getID(), receiverID.getID());
            if (firstCheck.getStatusCode() != HttpStatus.OK) return firstCheck;
            ResponseEntity<Object> secondCheck = this.friendsManager.addFriend(receiverID.getID(), senderID.getID());
            if (secondCheck.getStatusCode() != HttpStatus.OK) return secondCheck;
            this.invitationsManager.deleteInvitation(senderID.getID(), receiverID.getID());
            return new ResponseEntity<>("invitation successfully accepted!", HttpStatus.OK);
        } else {
            this.invitationsManager.deleteInvitation(senderID.getID(), receiverID.getID());
            return new ResponseEntity<>("invitation successfully declined!", HttpStatus.OK);
        }
    }

    /**
     * Deletes all correlated invitation with the user
     * @param sessionID of type String, the identifier used to reference account information
     * @return a ResponseEntity detailing successful handing of deletion or any associated error
     */
    public ResponseEntity<Object> deleteAllCorrelatedInvitations(String sessionID) {
        AccountID userID = this.accountManager.verifySession(new SessionID(sessionID));
        if (userID == null)
            return this.errorHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);

        List<Invitation> invitationsAsSender = this.invitationsManager.getAllBySenderID(userID.getID());
        List<Invitation> invitationsAsReceiver = this.invitationsManager.getAllByReceiverID(userID.getID());

        if (invitationsAsReceiver.size() + invitationsAsSender.size() == 0) {
            return this.errorHandler.logError(new NoSuchFileException("No Invitations correlated to the user!"), HttpStatus.NOT_FOUND);
        }

        this.invitationsManager.deleteAllInvitationsRelatedToUser(invitationsAsReceiver);
        this.invitationsManager.deleteAllInvitationsRelatedToUser(invitationsAsSender);

        return new ResponseEntity<>("Correlated Invitations all successfully deleted!", HttpStatus.OK);
    }
}
