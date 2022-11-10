package backend.usecases;

import backend.entities.IDs.AccountID;
import backend.entities.TaskCompletionRecord;

import java.sql.Timestamp;
import java.util.List;

public class TaskCompletionRecordTableManager {
    public boolean saveTaskCompletionRecord(TaskCompletionRecord record){
        return true;
    }
    public List<String> getCompletedTaskList(AccountID accountID){

    }
    public List<List<String>> getMostRecentList(List<String> friendIDs, Timestamp timestamp){

    }
}
