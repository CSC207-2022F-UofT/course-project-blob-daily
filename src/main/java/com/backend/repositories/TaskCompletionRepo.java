package com.backend.repositories;

import com.backend.entities.TaskCompletionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * completed task repository
 */
@Repository
public interface TaskCompletionRepo extends MongoRepository<TaskCompletionRecord, String>{
    /**
     * find the associated completion records with the given accountID
     * @param accountID of type String, references the account
     * @return a list of completion records by the account
     */
    @SuppressWarnings("unused")
    @Query("{ 'accountID': ?0 }")
    List<TaskCompletionRecord> findAllByAccountID(String accountID);

}
