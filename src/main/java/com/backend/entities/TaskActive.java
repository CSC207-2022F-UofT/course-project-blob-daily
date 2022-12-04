package com.backend.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * current active tasks
 */
@Document(collection = "ActiveTaskCollection")
public class TaskActive extends Task{
    //instance variable
    private final Date date;

    /**
     * Task active constructor
     * @param name of type String, the name of the task
     * @param reward of type double, the reward of the task
     * @param date of type Date, the date of the task
     */
    public TaskActive(String name, double reward, Date date){
        super(name, reward);
        this.date = date;
    }

    /**
     * gets the timestamp
     * @return a string of the timestamp
     */
    public String getDate() { return this.date.toString().substring(0, 10); }
}