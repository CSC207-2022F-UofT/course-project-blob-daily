package backend.entities;
import java.sql.Timestamp;

public class TaskCompletionRecord {
    private AccountID accountID;
    private Timestamp timestamp;
    private String taskName;

    public TaskCompletionRecord(AccountID accountID, Timestamp timestamp, String taskName){
        this.accountID = accountID;
        this.timestamp = timestamp;
        this.taskName = taskName;
    }

    public AccountID getAccountID(){
        return this.accountID;
    }
    public Timestamp getTimestamp(){
        return this.timestamp;
    }
    public String getTaskName(){
        return this.taskName;
    }
}
