package backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ActiveTaskCollection")
public class TaskActive extends Task{
    private final String timestamp;

    public TaskActive(String task, double reward, String timestamp){
        super(task, reward);
        this.timestamp = timestamp;
    }
    public String getTimestamp() {return this.timestamp;}
}
