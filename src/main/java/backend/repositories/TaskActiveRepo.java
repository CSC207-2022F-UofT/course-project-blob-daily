package backend.repositories;

import backend.entities.TaskActive;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskActiveRepo extends MongoRepository<TaskActive, String> {
}
