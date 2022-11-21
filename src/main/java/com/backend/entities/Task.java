package com.backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * the task itself
 */
@Document(collection = "TasksCollection")
public class Task {
    //instance variables
    private final String task;
    private final double reward;

    //constructor
    public Task(String task, double reward){
        this.task = task;
        this.reward = reward;
    }

    //getters
    /**
     * gets the task name
     * @return a string of the task name
     */
    public String getName(){
        return this.task;
    }

    /**
     * gets the task reward
     * @return a double of the task reward
     */
    public double getReward(){
        return this.reward;
    }
}
