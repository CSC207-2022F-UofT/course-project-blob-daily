package backend.repositories;

import backend.entities.TaskCompletionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCompletionRepo extends MongoRepository<TaskCompletionRecord, String>{
}
