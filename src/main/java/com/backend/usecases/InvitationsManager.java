package com.backend.usecases;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.InvitationsRepo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.security.InvalidParameterException;
import java.util.*;

@Service
@Configurable
public class InvitationsManager {
    private static InvitationsRepo invitationsRepo;

    public InvitationsManager(InvitationsRepo invitationsRepo) {
        InvitationsManager.invitationsRepo = invitationsRepo;
    }
    // check if invitation exists
    public static boolean invitationExists(String senderID, String receiverID) {
        return invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID) != null;
    }
    public static ResponseEntity<Object> checkUsersExist(AccountID sender, AccountID receiver) {
        if (sender == null && receiver == null) {
            return LogHandler.logError(new NullPointerException("Sender and receiver does not exist!"), HttpStatus.NOT_FOUND);
        } else if (sender == null) {
            return LogHandler.logError(new NullPointerException("Sender does not exist!"), HttpStatus.NOT_FOUND);
        } else if (receiver == null) return LogHandler.logError(new NullPointerException("Receiver does not exist!"), HttpStatus.NOT_FOUND);

        else return null;
    }

    public static ResponseEntity<Object> verifyInvitation(String currentUserName, String senderUsername, String receiverUsername, String sessionID) {

        // Check if valid session
        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) {
            return LogHandler.logError(new SessionException("Session does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(accountID).getBody())).getUsername().equals(currentUserName)) {
            return LogHandler.logError(new SessionException("Invalid session!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Update to use AccountManager
        AccountID sender = AccountManager.getAccountIDByUsername(senderUsername);
        AccountID receiver = AccountManager.getAccountIDByUsername(receiverUsername);

        // Check if users all meet criterion
        ResponseEntity<Object> result = checkUsersExist(sender, receiver);
        if (result != null) return result;

        String senderID = sender.getID();
        String receiverID = receiver.getID();

        // Check if sender and receiver are not the same
        if (senderID.equals(receiverID)) {
            throw new InvalidParameterException("Sender and Receiver are the same!");
        }

        ArrayList<String> senderAndReceiverID = new ArrayList<>();
        senderAndReceiverID.add(senderID);
        senderAndReceiverID.add(receiverID);

        return new ResponseEntity<>(senderAndReceiverID, HttpStatus.OK);
    }
    public static ResponseEntity<Object> deleteAllCorrelatedInvitations(String userName, String sessionID) {

        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(!((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(accountID).getBody())).getUsername().equals(userName)) {
            return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        assert accountID != null;
        List<Invitation> invitationsAsSender = invitationsRepo.findAllByReceiverID(accountID.getID());
        List<Invitation> invitationsAsReceiver = invitationsRepo.findAllByReceiverID(accountID.getID());

        invitationsRepo.deleteAllByReceiverID(invitationsAsReceiver);
        invitationsRepo.deleteAllBySenderID(invitationsAsSender);

        return new ResponseEntity<>("Correlated Invitations all successfully delete!", HttpStatus.OK);
    }

    // create invitation and save it to the database
    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> createInvitation(String senderUsername, String receiverUsername, String sessionID) {
        // check if invitation is valid
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(senderUsername, senderUsername, receiverUsername, sessionID);
        if(!(verification.getBody() instanceof ArrayList)) return verification;
        senderAndReceiverID = (ArrayList<String>) verification.getBody();

        String senderID = senderAndReceiverID.get(0);
        String receiverID = senderAndReceiverID.get(1);

        // Check if invitation already exists
        if (invitationExists(senderID, receiverID)) {
            return LogHandler.logError(new FileAlreadyExistsException("Invitation already sent!"), HttpStatus.PRECONDITION_FAILED);
        }

        // Check if sender and receiver are already friends
        if (FriendsManager.areFriends(senderID, receiverID)) {
            return LogHandler.logError(new FileAlreadyExistsException("Users are already friends!"), HttpStatus.PRECONDITION_FAILED);
        }

        // Create invitation
        Invitation invitation = new Invitation(senderID, receiverID, new Date(System.currentTimeMillis()));

        // Save invitation to DB
        invitationsRepo.save(invitation);
        return new ResponseEntity<>("Invitation successfully sent!", HttpStatus.CREATED);

    }

    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> withdrawInvitation(String senderUsername, String receiverUsername, String sessionID) {

        // check if invitation is valid
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(senderUsername, senderUsername, receiverUsername, sessionID);
        if(!(verification.getBody() instanceof ArrayList)) return verification;
        senderAndReceiverID = (ArrayList<String>) verification.getBody();

        String senderID = senderAndReceiverID.get(0);
        String receiverID = senderAndReceiverID.get(1);


        // Check if sender and receiver are not the same
        if (senderID.equals(receiverID)) {
            return LogHandler.logError(new InvalidParameterException("Sender and Receiver are the same!"), HttpStatus.PRECONDITION_FAILED);
        }

        // Check invitation exists in DB
        if(InvitationsManager.invitationExists(senderID, receiverID)) {
            // delete invitation
            return deleteInvitation(senderID, receiverID);
        } else {
            return LogHandler.logError(new Exception("Invitation does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete invitation from the database
    public static ResponseEntity<Object> deleteInvitation(String senderID, String receiverID) {
        invitationsRepo.deleteById(senderID + receiverID);
        return new ResponseEntity<>("Invitation successfully deleted!", HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> handleInvitation(String senderUsername, String receiverUsername, String sessionID, boolean isAccepted){
        // check if invitation & session is valid
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(receiverUsername, senderUsername, receiverUsername, sessionID);
        if(!(verification.getBody() instanceof ArrayList)) return verification;
        senderAndReceiverID = (ArrayList<String>) verification.getBody();

        String senderID = senderAndReceiverID.get(0);
        String receiverID = senderAndReceiverID.get(1);

        // check if invitation exists
        if (!invitationExists(senderID, receiverID)) {
            return LogHandler.logError(new NoSuchFileException("Invitation does not exist!"), HttpStatus.CONFLICT);
        }

        // Otherwise, update each other's friend's list and delete the existing invitation
        if (isAccepted) {
            ResponseEntity<Object> firstCheck = FriendsManager.addFriend(senderUsername, receiverUsername, sessionID);
            if(firstCheck.getStatusCode() != HttpStatus.OK) return firstCheck;
            ResponseEntity<Object> secondCheck = FriendsManager.addFriend(senderUsername, receiverUsername);
            if(secondCheck.getStatusCode() != HttpStatus.OK) return secondCheck;
            InvitationsManager.deleteInvitation(senderID, receiverID);
            return new ResponseEntity<>("invitation successfully accepted!", HttpStatus.OK);
        } else {
            InvitationsManager.deleteInvitation(senderID, receiverID);
            return new ResponseEntity<>("invitation successfully declined!", HttpStatus.OK);
        }
    }

    // receive an ArrayList of invitations of the user
    public static ResponseEntity<Object> getInvitations(String userName, String sessionID){

        // Check if valid session
        if(AccountManager.verifySession(new SessionID(sessionID)) == null) return LogHandler.logError(new SessionException("Invalid SessionID"), HttpStatus.BAD_REQUEST);

        AccountID userID = AccountManager.getAccountIDByUsername(userName);
        JSONObject invites = new JSONObject();

        assert userID != null;
        List<Invitation> accounts = invitationsRepo.findAllByReceiverID(userID.getID());
        JSONArray users = new JSONArray();

        for (Invitation account : accounts) {
            Account acc = (Account) AccountManager.getAccountInfo(new AccountID(account.getSenderID())).getBody();
            users.add(acc);
        }

        invites.put("invites", users);

        return new ResponseEntity<>(invites, HttpStatus.OK);

    }
}
