package com.questpets.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "ActiveTaskCollection")
public class TaskActive extends Task{
    private final Timestamp timestamp;

    public TaskActive(String task, double reward, Timestamp timestamp){
        super(task, reward);
        this.timestamp = timestamp;
    }
    public Timestamp getTimestamp() {return this.timestamp;}
}
