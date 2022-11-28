package com.backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * current active tasks
 */
@Document(collection = "ActiveTaskCollection")
public class TaskActive extends Task{
    //instance variable
    private final String timestamp;

    //constructor
    public TaskActive(String task, double reward, String timestamp){
        super(task, reward);
        this.timestamp = timestamp;
    }

    //getter
    /**
     * gets the timestamp
     * @return a string of the timestamp
     */
    public String getTimestamp() { return this.timestamp; }
}
