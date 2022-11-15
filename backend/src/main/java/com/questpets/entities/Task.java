package com.questpets.entities;

public class Task {
    private final String taskName;
    private final String description;
    private final double reward;

    public Task(String taskName, String description, double reward){
        this.taskName = taskName;
        this.description = description;
        this.reward = reward;
    }

    public String getTaskName(){
        return this.taskName;
    }
    public String getDescription(){
        return this.description;
    }
    public double getReward(){
        return this.reward;
    }
}
