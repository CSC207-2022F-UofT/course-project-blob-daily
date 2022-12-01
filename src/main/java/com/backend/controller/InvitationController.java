package com.backend.controller;

import com.backend.usecases.managers.InvitationsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvitationController {

    // create a get request to get invitations for a specific user
    @GetMapping("/friends/getInviteAsReceiver")
    public ResponseEntity<Object> getInvitationsAsReceiver(@RequestParam String sessionID) {
        // get sessionID from request
        // call getInvitations method from InvitationsManager
        // return response
        return InvitationsManager.getInvitations(sessionID, true);
    }

    @GetMapping("/friends/getInviteAsSender")
    public ResponseEntity<Object> getInvitationsAsSender(@RequestParam String sessionID) {
        // get sessionID from request
        // call getInvitations method from InvitationsManager
        // return response
        return InvitationsManager.getInvitations(sessionID, false);
    }

    // create a post request to send an invitation
    @PostMapping("/friends/sendInvite")
    public ResponseEntity<Object> sendInvitation(@RequestParam String receiverUsername, String sessionID) {
        // create invitation
        // save invitation to the database
        return InvitationsManager.createInvitation(receiverUsername, sessionID);
    }

    @DeleteMapping("/friends/withdrawInvite")
    public ResponseEntity<Object> withdrawInvitation(@RequestParam String receiverUsername,  String sessionID) {
        return InvitationsManager.withdrawInvitation(receiverUsername, sessionID);
    }

    // create a post request to accept an invitation
    @PostMapping("/friends/acceptInvite")
    public ResponseEntity<Object> acceptInvitation(@RequestParam String receiverUsername, String sessionID)  {
        return InvitationsManager.handleInvitation(receiverUsername, sessionID, true);
    }


    // create a post request to decline an invitation
    @DeleteMapping("/friends/declineInvite")
    public ResponseEntity<Object> declineInvitation(@RequestParam String receiverUsername, @RequestParam String sessionID) {
        // check if invitation exists
        // delete invitation from the database
        return InvitationsManager.handleInvitation(receiverUsername, sessionID,false);
    }

    @DeleteMapping("/friends/deleteAllCorrelatedInvitations")
    public ResponseEntity<Object> deleteAllCorrelatedInvitations(@RequestParam String sessionID) {
        return InvitationsManager.deleteAllCorrelatedInvitations(sessionID);
    }
}
