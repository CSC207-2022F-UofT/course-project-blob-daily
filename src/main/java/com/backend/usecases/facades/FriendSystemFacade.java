package com.backend.usecases.facades;

import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.FriendsManager;
import com.backend.usecases.managers.InvitationsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FriendSystemFacade {
    private final AccountManager accountManager;
    private final InvitationsManager invitationsManager;
    private final FriendsManager friendsManager;

    @Autowired
    public FriendSystemFacade(AccountManager accountManager, InvitationsManager invitationsManager, FriendsManager friendsManager) {
        this.accountManager = accountManager;
        this.invitationsManager = invitationsManager;
        this.friendsManager = friendsManager;
    }


    public ResponseEntity<Object> getUserFriends(String sessionID) {

    }

    public ResponseEntity<Object> deleteFriend(String friendUserName, String sessionID) {
    }

    public ResponseEntity<Object> deleteAllCorrelatedFriends(String sessionID) {
    }
}
