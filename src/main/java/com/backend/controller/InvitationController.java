package com.backend.controller;

import com.backend.entities.Invitation;
import com.backend.entities.users.DBAccount;
import com.backend.error.exceptions.ConditionException;
import com.backend.error.exceptions.CriteriaException;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.InvitationsManager;
import net.minidev.json.JSONObject;
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
    public JSONObject getInvitations(@RequestParam String username, @RequestParam String sessionID) {
        // get sessionID from request
        // call getInvitations method from InvitationsManager
        // return response
        return InvitationsManager.getInvitations(username);

    }

    // create a post request to send an invitation
    @PostMapping("/friends/sendInvite")
    public ResponseEntity<Object> sendInvitation(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam String sessionID) {
        // create invitation
        // save invitation to the database
        return InvitationsManager.createInvitation(senderUsername, receiverUsername, sessionID);
    }

    @DeleteMapping("/friends/withdrawInvite")
    public ResponseEntity<Object> withdrawInvitation(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam String sessionID) {
        return InvitationsManager.withdrawInvitation(senderUsername, receiverUsername, sessionID);
    }

    // create a post request to accept an invitation
    @PostMapping("/friends/acceptInvite")
    public ResponseEntity<Object> acceptInvitation(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam String sessionID)  {
        return InvitationsManager.handleInvitation(senderUsername, receiverUsername, sessionID, true);
    }


    // create a post request to decline an invitation
    @DeleteMapping("/friends/declineInvite")
    public ResponseEntity<Object> declineInvitation(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam String sessionID) {
        // check if invitation exists
        // delete invitation from the database
        return InvitationsManager.handleInvitation(senderUsername, receiverUsername, sessionID,false);
    }

    @DeleteMapping("/friends/deleteAll")
    public ResponseEntity<Object> deleteAllCorrelatedInvitations(@RequestParam String userName, @RequestParam String sessionID) {
        return InvitationsManager.deleteAllCorrelatedInvitations(userName, sessionID);
    }
}
