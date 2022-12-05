package com.backend.usecases.managers;

import com.backend.entities.Invitation;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.*;

@Service
@Configurable
public class InvitationsManager {

    private final InvitationsRepo invitationsRepo;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the Invitations Repository with errorHandler
     * @param invitationsRepo the repository dependency to be injected
     * @param errorHandler the ErrorHandler dependency to be injected
     */
    @SuppressWarnings("unused")
    @Autowired
    public InvitationsManager(InvitationsRepo invitationsRepo, IErrorHandler errorHandler) {
        this.invitationsRepo = invitationsRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Check if Invitation Exists
     * @param senderID of type String, senderID to reference the user as sender of Invitation
     * @param receiverID of type String, receiverID to reference the user as receiver of the Invitation
     * @return boolean whether Invitation with given parameter exists or not.
     */
    public boolean invitationExists(String senderID, String receiverID) {
        return invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID) != null;
    }

    /**
     * Helper function to check if Users Exists
     * @param senderID of type String, senderID to reference the user as sender of the Invitation
     * @param receiverID of type String, receiverID to reference the use as receiver of the Invitation
     * @return RespondEntity detailing the success of checking both users exists, or errors
     */
    public ResponseEntity<Object> checkUsersExist(String senderID, String receiverID) {
        if (senderID == null && receiverID == null) {
            return errorHandler.logError(new NullPointerException("Sender and receiver does not exist!"), HttpStatus.NOT_FOUND);
        } else if (senderID == null) {
            return errorHandler.logError(new NullPointerException("Sender does not exist!"), HttpStatus.NOT_FOUND);
        } else if (receiverID == null){
            return errorHandler.logError(new NullPointerException("Receiver does not exist!"), HttpStatus.NOT_FOUND);
        }
        return null;
    }

    /**
     * Checks if the Invitation is a valid invitation
     * @param invitation as Invitation, the Object to be verified
     * @return ResponseEntity Object detailing the success of verification or errors
     */
    public ResponseEntity<Object> verifyInvitation(Invitation invitation) {

        // check if the invitation follows the entities structure
        if (invitation.getReceiverID() == null) return errorHandler.logError(new InvalidParameterException("Invalid receiverID"), HttpStatus.CONFLICT);
        if (invitation.getSenderID() == null) return errorHandler.logError(new InvalidParameterException("Invalid senderID"), HttpStatus.CONFLICT);
        if (invitation.getTimestamp() == null) return errorHandler.logError(new InvalidParameterException("Invalid TimeStamp"), HttpStatus.CONFLICT);
        String senderID = invitation.getSenderID();
        String receiverID = invitation.getReceiverID();

        // Check if users all meet criterion
        ResponseEntity<Object> result = checkUsersExist(senderID, receiverID);
        if (result != null) return result;

        // Check if sender and receiver are not the same
        if (senderID.equals(receiverID)) {
            return errorHandler.logError(new InvalidParameterException("Sender and Receiver are the same!"), HttpStatus.CONFLICT);
        }

        ArrayList<String> senderAndReceiverID = new ArrayList<>();
        senderAndReceiverID.add(senderID);
        senderAndReceiverID.add(receiverID);

        return new ResponseEntity<>(senderAndReceiverID, HttpStatus.OK);
    }

    /**
     * Saves Invitation to the Collection
     * @param invitation as Invitation Object, the parameter to be saved in the Collection
     */
    public void saveInvitation(Invitation invitation) {
        invitationsRepo.save(invitation);
    }

    /**
     * Delete invitation from the database
     * @param accepterID of type String, accepterID to reference the user who is receiving the Invitation
     * @param senderID of type String, sender to reference the user who is sending the Invitation
     * @return ResponseEntity Object detailing the success of deletion or errors
     */
    public ResponseEntity<Object> deleteInvitation(String accepterID, String senderID) {
        invitationsRepo.deleteById(senderID + accepterID);
        return new ResponseEntity<>("Invitation successfully deleted!", HttpStatus.OK);
    }

    /**
     * Receives list of invitations with user as receiver
     * @param userID of type String, userID to reference as corresponding user
     * @return List of Invitation with user as receiver or null
     */
    public List<Invitation> getAllByReceiverID(String userID) {
        return invitationsRepo.findAllByReceiverID(userID);
    }

    /**
     * Receives list of invitations with user as sender
     * @param userID of type String, userID to reference as corresponding user
     * @return List of Invitation with user as sender or null
     */
    public List<Invitation> getAllBySenderID(String userID) {
        return invitationsRepo.findAllBySenderID(userID);
    }

    /**
     * Delete all Invitations containing userID as receiver or sender
     * @param invitations of type List, List of invitations to be deleted from the Invitations Collection
     */
    public void deleteAllInvitationsRelatedToUser(List<Invitation> invitations) {
        invitationsRepo.deleteAll(invitations);
    }

}
