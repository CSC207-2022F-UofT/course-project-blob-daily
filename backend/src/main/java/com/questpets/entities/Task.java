package com.questpets.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TasksCollection")
public class Task {
    private final String task;
    private final double reward;

    public Task(String task, double reward){
        this.task = task;
        this.reward = reward;
    }

    public String getName(){
        return this.task;
    }
    public double getReward(){
        return this.reward;
    }
}
