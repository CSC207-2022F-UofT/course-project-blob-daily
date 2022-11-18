package com.backend.controller;

import com.backend.entities.invitations.Invitation;
import com.backend.error.exceptions.ConditionException;
import com.backend.error.exceptions.CriteriaException;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.InvitationsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitationController {
    public static InvitationsRepo invitationsRepo;

    public InvitationController(InvitationsRepo invitationsRepo) {
        InvitationController.invitationsRepo = invitationsRepo;
    }

    // create a get request to get invitations for a specific user
    @GetMapping("/friends/getInvite")
    public List<String> getInvitations(@RequestParam String username, @RequestParam boolean isReceiver) throws CriteriaException {
        // get sessionID from request
        // call getInvitations method from InvitationsManager
        // return response
        return InvitationsManager.getInvitations(username, isReceiver);

    }

    // create a post request to send an invitation
    @PostMapping("/friends/sendInvite")
    public ResponseEntity<Object> sendInvitation(@RequestParam String senderUsername, @RequestParam String receiverUsername) throws ConditionException, CriteriaException {
        // create invitation
        // save invitation to the database
        return InvitationsManager.createInvitation(senderUsername, receiverUsername);
    }

    // create a post request to accept an invitation
    @PostMapping("/friends/acceptInvite")
    public ResponseEntity<Object> acceptInvitation(@RequestParam String senderID, @RequestParam String receiverID) throws ConditionException, CriteriaException {
        // check if invitation exists

        // check if sender and receiver are the same
        // check if sender and receiver exist
        // delete invitation from the database
        // add sender to receiver's friends list
        // add receiver to sender's friends list
        return InvitationsManager.handleInvitation(senderID, receiverID, true);
    }


    // create a post request to decline an invitation
    @DeleteMapping("/friends/declineInvite")
    public ResponseEntity<Object> declineInvitation(@RequestParam String senderID, @RequestParam String receiverID) throws ConditionException, CriteriaException {
        // check if invitation exists
        // delete invitation from the database
        return InvitationsManager.handleInvitation(senderID, receiverID, false);
    }

    @DeleteMapping("/friends/deleteAll")
    public ResponseEntity<Object> deleteAllCorrelatedInvitations(@RequestParam String userID) throws CriteriaException {
        return InvitationsManager.deleteAllCorrelatedInvitations(userID);
    }

    @GetMapping("/friends/getAll")
    public List<Invitation> getAllInvitations() throws CriteriaException {
        return InvitationsManager.getAllInvitations();
    }
}
