package com.backend.repositories;

import com.backend.entities.TaskCompletionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCompletionRepo extends MongoRepository<TaskCompletionRecord, String>{

    @Query("{ 'accountID': ?0 }")
    List<TaskCompletionRecord> findAllByAccountID(String accountID);

}
