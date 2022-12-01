package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
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
    private static ResponseEntity<Object> checkUsersExist(AccountID sender, AccountID receiver) {
        if (sender == null && receiver == null) {
            return LogHandler.logError(new NullPointerException("Sender and receiver does not exist!"), HttpStatus.NOT_FOUND);
        } else if (sender == null) {
            return LogHandler.logError(new NullPointerException("Sender does not exist!"), HttpStatus.NOT_FOUND);
        } else if (receiver == null) return LogHandler.logError(new NullPointerException("Receiver does not exist!"), HttpStatus.NOT_FOUND);

        else return null;
    }

    private static ResponseEntity<Object> verifyInvitation(String senderUsername, String receiverUsername) {

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
            return LogHandler.logError(new InvalidParameterException("Sender and Receiver are the same!"), HttpStatus.CONFLICT);
        }

        ArrayList<String> senderAndReceiverID = new ArrayList<>();
        senderAndReceiverID.add(senderID);
        senderAndReceiverID.add(receiverID);

        return new ResponseEntity<>(senderAndReceiverID, HttpStatus.OK);
    }
    public static ResponseEntity<Object> deleteAllCorrelatedInvitations(String sessionID) {

        AccountID accountID = AccountManager.verifySession(new SessionID(sessionID));
        if(accountID == null) return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);

        List<Invitation> invitationsAsSender = invitationsRepo.findAllBySenderID(accountID.getID());
        List<Invitation> invitationsAsReceiver = invitationsRepo.findAllByReceiverID(accountID.getID());

        if(invitationsAsReceiver.size() + invitationsAsSender.size() == 0){
            return LogHandler.logError(new NoSuchFileException("No Invitations correlated to the user!"), HttpStatus.NOT_FOUND);
        }

        invitationsRepo.deleteAll(invitationsAsReceiver);
        invitationsRepo.deleteAll(invitationsAsSender);

        return new ResponseEntity<>("Correlated Invitations all successfully deleted!", HttpStatus.OK);
    }

    // create invitation and save it to the database
    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> createInvitation(String receiverUsername, String sessionID) {
        System.out.println(sessionID);
        // check if invitation is valid
        AccountID userID = AccountManager.verifySession(new SessionID(sessionID));
        if(userID == null) return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);
        String senderUsername = ((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(userID).getBody())).getUsername();
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(senderUsername, receiverUsername);
        if(!(verification.getBody() instanceof ArrayList)) return verification;
        senderAndReceiverID = (ArrayList<String>) verification.getBody();

        String senderID = senderAndReceiverID.get(0);
        String receiverID = senderAndReceiverID.get(1);

        // Check if invitation already exists
        if (invitationExists(senderID, receiverID)) {
            return LogHandler.logError(new FileAlreadyExistsException("Invitation already sent!"), HttpStatus.PRECONDITION_FAILED);
        }

        // check if the other user already sent invitation
        if(invitationExists(receiverID, senderID)) {
            // if then, the automatically accept each other
            handleInvitation(receiverUsername, sessionID, true);
            return new ResponseEntity<Object>("Friend request automatically accepted!", HttpStatus.OK);
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
    public static ResponseEntity<Object> withdrawInvitation(String receiverUsername, String sessionID) {

        AccountID userID = AccountManager.verifySession(new SessionID(sessionID));
        if(userID == null) return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);
        String senderUsername = ((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(userID).getBody())).getUsername();
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(senderUsername, receiverUsername);
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
    public static ResponseEntity<Object> deleteInvitation(String accepterID, String senderID) {
        System.out.println(accepterID + senderID);
        invitationsRepo.deleteById(senderID + accepterID);
        return new ResponseEntity<>("Invitation successfully deleted!", HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    public static ResponseEntity<Object> handleInvitation(String receiverUsername, String sessionID, boolean isAccepted){
        AccountID userID = AccountManager.verifySession(new SessionID(sessionID));
        if(userID == null) return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);
        String senderUsername = ((ProtectedAccount) Objects.requireNonNull(AccountManager.getAccountInfo(userID).getBody())).getUsername();
        ArrayList<String> senderAndReceiverID;
        ResponseEntity<Object> verification = InvitationsManager.verifyInvitation(receiverUsername, senderUsername);
        if(!(verification.getBody() instanceof ArrayList)) return verification;
        senderAndReceiverID = (ArrayList<String>) verification.getBody();

        String senderID = senderAndReceiverID.get(0);
        String receiverID = senderAndReceiverID.get(1);

        // check if invitation exists
        if (!invitationExists(senderID, receiverID)) {
            return LogHandler.logError(new NoSuchFileException("Invitation does not exist!"), HttpStatus.CONFLICT);
        }

        // Otherwise, update each other's friend's list and delete the existing invitation
        // and check if the other user already sent invitation
        if (isAccepted) {
            // addFriend
            ResponseEntity<Object> firstCheck = FriendsManager.addFriend(senderID, receiverID);
            if(firstCheck.getStatusCode() != HttpStatus.OK) return firstCheck;
            ResponseEntity<Object> secondCheck = FriendsManager.addFriend(receiverID, senderID);
            if(secondCheck.getStatusCode() != HttpStatus.OK) return secondCheck;
            InvitationsManager.deleteInvitation(receiverID, senderID);
            return new ResponseEntity<>("invitation successfully accepted!", HttpStatus.OK);
        } else {
            InvitationsManager.deleteInvitation(receiverID, senderID);
            return new ResponseEntity<>("invitation successfully declined!", HttpStatus.OK);
        }
    }

    // receive an ArrayList of invitations of the user
    public static ResponseEntity<Object> getInvitations(String sessionID, boolean isReceiver){

        // Check if valid session
        AccountID userID = AccountManager.verifySession(new SessionID(sessionID));
        if(userID == null) return LogHandler.logError(new SessionException("Invalid SessionID!"), HttpStatus.NOT_FOUND);

        JSONObject invites = new JSONObject();
        List<Invitation> accounts;

        if(isReceiver) {
            accounts = invitationsRepo.findAllByReceiverID(userID.getID());
        }
        else {
            accounts = invitationsRepo.findAllBySenderID(userID.getID());
        }
        JSONArray users = new JSONArray();

        for (Invitation account : accounts) {
            ProtectedAccount acc = (ProtectedAccount) AccountManager.getAccountInfo(new AccountID(account.getSenderID())).getBody();
            users.add(acc);
        }

        invites.put("invites", users);

        return new ResponseEntity<>(invites, HttpStatus.OK);

    }
}
