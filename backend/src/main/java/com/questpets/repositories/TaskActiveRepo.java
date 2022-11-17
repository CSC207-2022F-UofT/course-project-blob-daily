package com.questpets.repositories;

import com.questpets.entities.TaskActive;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskActiveRepo extends MongoRepository<TaskActive, String> {
}
