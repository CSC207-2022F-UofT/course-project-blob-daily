package backend.usecases;

import backend.entities.IDs.AccountID;
import backend.entities.IDs.SessionID;
import backend.entities.Task;
import backend.entities.TaskCompletionRecord;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Random;

public class TaskManager {
    //pass taskcompletionrecord
    List<Task> tasks = new ArrayList<>();
    static List<Task> active = new ArrayList<>();
    static List<TaskCompletionRecord> complete = new ArrayList<>();
    public static boolean uploadImage(SessionID sessionID, int bson, String taskName){
        try {

            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public void setTaskList(){
        this.tasks = taskRepository();
    }
    public void setCompleteTasks(){
        complete = taskCompletionRepository();
    }
    public void setActiveTasks(){
        active.clear();
        Random rand = new Random();
        while (active.size() <= 3) {
            int a = rand.nextInt(tasks.size()) + 1;
            Task t = tasks.get(a);
            if (!active.contains(t)) {
                active.add(t);
            }
        }
    }
    public static List<Task> getActiveTasks(){
        return active;
    }
    public static List<TaskCompletionRecord> getCompleteTasks(SessionID sessionID) {
        return complete;
    }
}
