package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.InvitationController;
import com.backend.entities.Invitation;
import com.backend.entities.users.DBAccount;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public class InvitationsManager {
    // check if invitation exists
    public static boolean invitationExists(String senderID, String receiverID) {
        System.out.println("Hello from the other side");
        return InvitationController.invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID) != null;
    }

    public static ResponseEntity<Object> deleteAllCorrelatedInvitations(String userName, String sessionID) {
        try {
            String userID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
            List<Invitation> invitationsAsSender = InvitationController.invitationsRepo.findAllByReceiverID(userID);
            List<Invitation> invitationsAsReceiver = InvitationController.invitationsRepo.findAllByReceiverID(userID);

            InvitationController.invitationsRepo.deleteAllByReceiverID(invitationsAsReceiver);
            InvitationController.invitationsRepo.deleteAllBySenderID(invitationsAsSender);

            return new ResponseEntity<>("Correlated Invitations all successfully delete!", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<Object> checkUsersExist(DBAccount sender, DBAccount receiver) {
        if (sender == null && receiver == null) {
            return new ResponseEntity<>("sender and receiver does not exists!", HttpStatus.CONFLICT);
        } else if (sender == null) {
            return new ResponseEntity<>("sender does not exists!", HttpStatus.CONFLICT);
        } else if (receiver == null) return new ResponseEntity<>("receiver does not exists!", HttpStatus.CONFLICT);

        else return null;
    }

    // create invitation and save it to the database
    public static ResponseEntity<Object> createInvitation(String senderUsername, String receiverUsername, String sessionID) {

        // Update to use AccountManager
        DBAccount sender = AccountController.accountsRepo.findByUsername(senderUsername);
        DBAccount receiver = AccountController.accountsRepo.findByUsername(receiverUsername);

        try {
            // check if sender and receiver exist
            ResponseEntity<Object> result = checkUsersExist(sender, receiver);
            if (result != null) return result;

            String senderID = sender.getAccountID();
            String receiverID = receiver.getAccountID();

            // check if sender and receiver are not the same
            if (senderID.equals(receiverID)) {
                return new ResponseEntity<>("sender and receiver are the same!", HttpStatus.CONFLICT);
            }

            // check if invitation already exists
            if (invitationExists(senderID, receiverID)) {
                return new ResponseEntity<>("invitation already sent!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // check if sender and receiver are already friends
            if (FriendsManager.areFriends(senderID, receiverID)) {
                return new ResponseEntity<>("sender and receiver are already friends!", HttpStatus.CONFLICT);
            }

            // create invitation
            Invitation invitation = new Invitation(senderID, receiverID, new Date(System.currentTimeMillis()));

            // save invitation to the database
            InvitationController.invitationsRepo.save(invitation);
            return new ResponseEntity<>("Invitation successfully sent!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<Object> withdrawInvitation(String senderUsername, String receiverUsername, String sessionID) {
        try {


            String senderID = AccountController.accountsRepo.findByUsername(senderUsername).getAccountID();
            String receiverID  = AccountController.accountsRepo.findByUsername(receiverUsername).getAccountID();


            // delete invitation from the database
            if(InvitationsManager.invitationExists(senderID, receiverID)) {
                deleteInvitation(senderID, receiverID);
                return new ResponseEntity<>("Invitation successfully withdrawn!", HttpStatus.OK);
            }

            return new ResponseEntity<>("Invitation does not exist!", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete invitation from the database
    public static ResponseEntity<Object> deleteInvitation(String senderID, String receiverID) {
        try {
            // delete invitation from the database
            InvitationController.invitationsRepo.deleteById(senderID + receiverID);
            return new ResponseEntity<>("Invitation successfully deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<Object> handleInvitation(String senderUsername, String receiverUsername, String sessionID, boolean isAccepted){
        try {

            DBAccount sender = AccountController.accountsRepo.findByUsername(senderUsername);
            DBAccount receiver = AccountController.accountsRepo.findByUsername(receiverUsername);
            System.out.println(sender);
            System.out.println(receiver);

            // check if sender and reciever exists
            ResponseEntity<Object> result = checkUsersExist(sender, receiver);
            if (result != null) return result;

            System.out.println("handle invitation gets to here!");

            String senderID = sender.getAccountID();
            String receiverID = receiver.getAccountID();

            // check if invitation exists
            if (!invitationExists(senderID, receiverID)) {
                return new ResponseEntity<>("invitation does not exists!", HttpStatus.CONFLICT);
            }

            // Otherwise, update each other's friend's list and delete the existing invitation
            if (isAccepted) {
                FriendsManager.addFriend(senderUsername, receiverUsername);
                FriendsManager.addFriend(receiverUsername, senderUsername);
                InvitationsManager.deleteInvitation(senderID, receiverID);
                return new ResponseEntity<>("invitation successfully accepted!", HttpStatus.OK);
            } else {
                InvitationsManager.deleteInvitation(senderID, receiverID);
                return new ResponseEntity<>("invitation successfully declined!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // receive an ArrayList of invitations of the receiver
    public static JSONObject getInvitations(String userName){
        try {
            DBAccount user = AccountController.accountsRepo.findByUsername(userName);
            JSONObject invites = new JSONObject();
            // If user is receiver
            List<Invitation> accounts = InvitationController.invitationsRepo.findAllByReceiverID(user.getAccountID());
            JSONArray users = new JSONArray();

            for (Invitation account : accounts) {
                DBAccount acc = AccountController.accountsRepo.findByAccountID(account.getSenderID());
                System.out.println(acc.getUsername());
                users.add(acc);
            }

            invites.put("invites", users);

            return invites;

        } catch (Exception e) {
            return null;
        }

    }
}
