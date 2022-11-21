package com.backend.repositories;
import com.backend.entities.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface InvitationsRepo extends MongoRepository<Invitation, String> {

    @Query("{ 'senderID' : ?0, 'receiverID' : ?1 }")
    Invitation findBySenderIDAndReceiverID(String senderID, String receiverID);

    @Query("{ 'receiverID' : ?0 }")
    List<Invitation> findAllByReceiverID(String receiverID);

    @Query("{ 'senderID' : { $regex: ?0} }")
    List<Invitation> findAllBySenderID(String senderID);

    @Query("{ 'receiverID' : ?0}")
    void deleteAllByReceiverID(List<Invitation> invitationsAsReceiver);

    @Query("{ 'senderID' : ?0}")
    void deleteAllBySenderID(List<Invitation> invitationsAsSender);
}
