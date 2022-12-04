package com.backend.repositories;

import com.backend.entities.TaskActive;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Active task repository
 */
@Repository
public interface TaskActiveRepo extends MongoRepository<TaskActive, String> {
}