package com.backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * the task itself
 */
@Document(collection = "TasksCollection")
public class Task {
    //instance variables
    private final String name;
    private final double reward;

    /**
     * Task constructor
     * @param name of type String, the name of the task
     * @param reward of type double, the reward of the task
     */
    public Task(String name, double reward){
        this.name = name;
        this.reward = reward;
    }

    /**
     * gets the task name
     * @return a string of the task name
     */
    public String getName(){
        return this.name;
    }

    /**
     * gets the task reward
     * @return a double of the task reward
     */
    public double getReward(){
        return this.reward;
    }
}