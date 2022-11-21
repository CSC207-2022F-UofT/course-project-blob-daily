package com.backend.repositories;

import com.backend.entities.TaskCompletionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface FeedRepo extends MongoRepository<TaskCompletionRecord, String> {
    @Query("{accountID :?0}")
    List<TaskCompletionRecord> findByAccountID(String accountID);
}
