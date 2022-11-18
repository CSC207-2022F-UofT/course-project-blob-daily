package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.InvitationController;
import com.backend.entities.invitations.Invitation;
import com.backend.entities.users.DBAccount;
import com.backend.error.exceptions.ConditionException;
import com.backend.error.exceptions.CriteriaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvitationsManager {
    // check if invitation exists
    public static boolean invitationExists(String senderID, String receiverID) {
        return InvitationController.invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID) != null;
    }

    public static ResponseEntity<Object> deleteAllCorrelatedInvitations(String userName) {
        try {
            String userID = AccountController.accountsRepo.findByUsername(userName).getAccountID();
            List<Invitation> invitationsAsSender = InvitationController.invitationsRepo.findAllByReceiverID(userID);
            List<Invitation> invitationsAsReceiver = InvitationController.invitationsRepo.findAllByReceiverID(userID);

            InvitationController.invitationsRepo.deleteAll(invitationsAsReceiver);
            InvitationController.invitationsRepo.deleteAll(invitationsAsSender);

            return new ResponseEntity<>("Correlated Invitations all successfully delete!", HttpStatus.OK);

        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static ResponseEntity<Object> checkUsersExist(DBAccount sender, DBAccount receiver) {
        if (sender == null && receiver == null) {
            return new ResponseEntity<>("sender and receiver does not exists!", HttpStatus.CONFLICT);
        }
        else if(sender == null) {
            return new ResponseEntity<>("sender does not exists!", HttpStatus.CONFLICT);
        }
        else if(receiver == null) return new ResponseEntity<>("receiver does not exists!", HttpStatus.CONFLICT);

        else return null;
    }

    // create invitation and save it to the database
    public static ResponseEntity<Object> createInvitation(String senderUsername, String receiverUsername) throws CriteriaException, ConditionException {

        // waiting for SHaan to create these methods in accountsmanager
        DBAccount sender = AccountController.accountsRepo.findByUsername(senderUsername);
        DBAccount receiver = AccountController.accountsRepo.findByUsername(receiverUsername);

        try {
            // check if sender and receiver exist
            ResponseEntity<Object> result = checkUsersExist(sender, receiver);
            if(result != null) return result;

            String senderID = sender.getAccountID();
            String receiverID = receiver.getAccountID();

            // check if sender and receiver are not the same
            if (senderID.equals(receiverID)) {
                return new ResponseEntity<>("sender and receiver are the same!", HttpStatus.CONFLICT);
            }

            // check if invitation already exists
            if(invitationExists(senderID, receiverID)) {
                return new ResponseEntity<>("invitation already sent!", HttpStatus.INTERNAL_SERVER_ERROR);
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

    // delete invitation from the database
    public static void deleteInvitation(String senderID, String receiverID) throws CriteriaException, ConditionException {
        // check if invitation exists
        if(!invitationExists(senderID, receiverID)) {
            throw new CriteriaException("Invitation does not exist");
        }

        // delete invitation from the database
        InvitationController.invitationsRepo.deleteById(senderID.toString() + receiverID);
    }

    public static ResponseEntity<Object> handleInvitation(String senderUsername, String receiverUsername, boolean isAccepted) throws CriteriaException, ConditionException {
        try{

            DBAccount sender = AccountController.accountsRepo.findByUsername(senderUsername);
            DBAccount receiver = AccountController.accountsRepo.findByUsername(receiverUsername);

            // check if sender and reciever exists
            ResponseEntity<Object> result = checkUsersExist(sender, receiver);
            if(result != null) return result;


            String senderID = sender.getAccountID();
            String receiverID = receiver.getAccountID();

            // check if invitation exists
            if(!invitationExists(senderID, receiverID)) {
                throw new ConditionException("Invitation does not exist!");
            }

            // Otherwise, update each other's friend's list and delete the existing invitation
            // TBI
            if(isAccepted) {
                FriendsManager.addFriend(senderUsername, receiverUsername);
                Invitation invitation = InvitationController.invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID);
                InvitationController.invitationsRepo.delete(invitation);
                return new ResponseEntity<>("invitation successfully accepted!", HttpStatus.OK);
            }
            else {
                Invitation invitation = InvitationController.invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID);
                InvitationController.invitationsRepo.delete(invitation);
                return new ResponseEntity<>("invitation successfully declined!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // receive an ArrayList of invitations of the receiver
    public static List<String> getInvitations(String userName, boolean position) throws CriteriaException {
        try
        {
            DBAccount userID = AccountController.accountsRepo.findByUsername(userName);
            ArrayList<String> invitations = new ArrayList<>();

            System.out.println(userID.getAccountID());
            System.out.println(position);
            // If user is receiver
            if(position) {
                System.out.println("Get invitations as receiver");
                List<Invitation> accounts = InvitationController.invitationsRepo.findAllByReceiverID(userID.getAccountID());
                System.out.println("Does it run this?");
                System.out.println(accounts.size());
                for(Invitation account: accounts) {
                    System.out.println("SenderID is: " + account.getSenderID());
                    invitations.add(account.getSenderID());
                }
            }
            // If the user is sender
            else {
                List<Invitation> accounts = InvitationController.invitationsRepo.findAllBySenderID(userID.getAccountID());
                for(Invitation account: accounts) {
                    invitations.add(account.getReceiverID());
                }
            }
            System.out.println(invitations);
            return invitations;

        } catch (Exception e) {
            return null;
        }

    }

    public static List<Invitation> getAllInvitations() {
        return InvitationController.invitationsRepo.findAll();
    }
}
