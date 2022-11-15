package com.questpets.usecases;

import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.Task;
import com.questpets.entities.TaskCompletionRecord;
import com.questpets.repositories.TaskCompletionRepo;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private static List<Task> active = new ArrayList<>();
    private static List<TaskCompletionRecord> complete = new ArrayList<>();
    @Autowired
    private static TaskCompletionRepo taskRepo;

//    public void setTaskList() {
//        this.tasks = taskRepository();
//    }
//
//    public void setCompleteTasks() {
//        complete = taskCompletionRepository();
//    }

    public static boolean postCompletedTask(AccountID account, Timestamp timestamp, String name, String image){
        try {
            TaskCompletionRecord record = new TaskCompletionRecord(account, timestamp, name, image);
            taskRepo.save(record);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(account + " " + timestamp + " " + name + " " + image);
            return false;
        }
    }

    public void setActiveTasks() {
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

    public static List<Task> getActiveTasks() {
        //active.add(new Task("take a shower", "please take a shower its been 4 days", 100));
        //if next day call setactivetasks
        return active;
    }

    public static List<TaskCompletionRecord> getCompleteTasks(SessionID sessionID) {
        return complete;
    }
}