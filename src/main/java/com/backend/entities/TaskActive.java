package com.backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * current active tasks
 */
@Document(collection = "ActiveTaskCollection")
public class TaskActive extends Task{
    //instance variable
    private final String timestamp;

    /**
     * Task active constructor
     * @param name of type String, the name of the task
     * @param reward of type double, the reward of the task
     * @param timestamp of type String, the time of the task
     */
    public TaskActive(String name, double reward, String timestamp){
        super(name, reward);
        this.timestamp = timestamp;
    }

    /**
     * gets the timestamp
     * @return a string of the timestamp
     */
    public String getTimestamp() { return this.timestamp; }
}
