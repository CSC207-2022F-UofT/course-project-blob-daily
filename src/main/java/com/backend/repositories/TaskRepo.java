package com.backend.repositories;

import com.backend.entities.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Task repository
 */
@Repository
public interface TaskRepo extends MongoRepository<Task, String> {
}
