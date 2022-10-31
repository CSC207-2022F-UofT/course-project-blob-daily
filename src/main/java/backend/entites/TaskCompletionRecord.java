package backend.entites;

public class TaskCompletionRecord {
    private String accountID;
    private String timestamp;
    private String taskName;

    public TaskCompletionRecord(String accountID, String timestamp, String taskName){
        this.accountID = accountID;
        this.timestamp = timestamp;
        this.taskName = taskName;
    }

    public String getAccountID(){
        return this.accountID;
    }
    public String getTimestamp(){
        return this.timestamp;
    }
    public String getTaskName(){
        return this.taskName;
    }
}
