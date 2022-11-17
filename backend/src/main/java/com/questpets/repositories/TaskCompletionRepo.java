package com.questpets.repositories;

import com.questpets.entities.TaskCompletionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCompletionRepo extends MongoRepository<TaskCompletionRecord, String>{
}