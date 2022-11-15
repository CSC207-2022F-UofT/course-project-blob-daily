package com.questpets.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCompletionRepo extends MongoRepository<com.questpets.entities.TaskCompletionRecord, String>{

}
