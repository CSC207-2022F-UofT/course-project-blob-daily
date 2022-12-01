package com.backend.usecases.managers;

import com.backend.entities.Invitation;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.InvitationsRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public boolean invitationExists(String senderID, String receiverID) {
        return invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID) != null;
    }

    private static ResponseEntity<Object> checkUsersExist(String sender, String receiver) {
        if (sender == null && receiver == null) {
            return LogHandler.logError(new NullPointerException("Sender and receiver does not exist!"), HttpStatus.NOT_FOUND);
        } else if (sender == null) {
            return LogHandler.logError(new NullPointerException("Sender does not exist!"), HttpStatus.NOT_FOUND);
        } else if (receiver == null)
            return LogHandler.logError(new NullPointerException("Receiver does not exist!"), HttpStatus.NOT_FOUND);

        else return null;
    }

    public ResponseEntity<Object> verifyInvitation(Invitation invitation) {

        // check if the invitation follows the entities structure
        if (invitation.getReceiverIDObject() == null) return LogHandler.logError(new InvalidParameterException("Invalid receiverID"), HttpStatus.CONFLICT);
        if (invitation.getSenderIDObject() == null) return LogHandler.logError(new InvalidParameterException("Invalid senderID"), HttpStatus.CONFLICT);
        if (invitation.getTimestamp() == null) return LogHandler.logError(new InvalidParameterException("Invalid TimeStamp"), HttpStatus.CONFLICT);
        String senderID = invitation.getSenderID();
        String receiverID = invitation.getReceiverID();

        // Check if users all meet criterion
        ResponseEntity<Object> result = checkUsersExist(senderID, receiverID);
        if (result != null) return result;

        // Check if sender and receiver are not the same
        if (senderID.equals(receiverID)) {
            return LogHandler.logError(new InvalidParameterException("Sender and Receiver are the same!"), HttpStatus.CONFLICT);
        }

        ArrayList<String> senderAndReceiverID = new ArrayList<>();
        senderAndReceiverID.add(senderID);
        senderAndReceiverID.add(receiverID);

        return new ResponseEntity<>(senderAndReceiverID, HttpStatus.OK);
    }

    public void saveInvitation(Invitation invitation) {
        invitationsRepo.save(invitation);
    }

    // delete invitation from the database
    public ResponseEntity<Object> deleteInvitation(String accepterID, String senderID) {
        System.out.println(accepterID + senderID);
        invitationsRepo.deleteById(senderID + accepterID);
        return new ResponseEntity<>("Invitation successfully deleted!", HttpStatus.OK);
    }

    public List<Invitation> getAllByReceiverID(String userID) {
        return invitationsRepo.findAllByReceiverID(userID);
    }

    public List<Invitation> getAllBySenderID(String userID) {
        return invitationsRepo.findAllBySenderID(userID);
    }

    public void deleteAllInvitationsRelatedTo(List<Invitation> invitations) {
        invitationsRepo.deleteAll(invitations);
    }

}
