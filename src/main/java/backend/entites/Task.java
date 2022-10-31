package backend.entites;

public class Task {
    private String taskName;
    private String description;
    private double reward;

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
