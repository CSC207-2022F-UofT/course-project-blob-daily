package com.backend.repositories;
import com.backend.entities.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Mongo Repository for custom queries on the Invitations Collection
 */
public interface InvitationsRepo extends MongoRepository<Invitation, String> {
    /**
     * Find the specific Invitation with the given parameter (by senderID and receiverID)
     * @param senderID of type String, accountID to reference the user as sender
     * @param receiverID of type String, accountID to reference the user as receiver
     * @return the retrieved Invitation object given valid parameters and exists (otherwise null)
     */
    @Query("{ 'senderID' : ?0, 'receiverID' : ?1 }")
    Invitation findBySenderIDAndReceiverID(String senderID, String receiverID);

    /**
     * Find a list of associated Invitation with the given parameter(by receiverID)
     * @param receiverID of type String, accountID to reference the user as the receiver
     * @return the retrieved list of Invitation Objects given valid parameters and exists (otherwise null)
     */
    @Query("{ 'receiverID' : ?0 }")
    List<Invitation> findAllByReceiverID(String receiverID);

    /**
     * Find a list of associated Invitation with the given parameter(by senderID)
     * @param senderID of type String, accountID to reference the user as the senderID
     * @return the retrieved list of Invitation Objects given valid parameters and exists (otherwise null)
     */
    @Query("{ 'senderID' : ?0 }")
    List<Invitation> findAllBySenderID(String senderID);
}
