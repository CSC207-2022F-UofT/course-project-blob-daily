package com.questpets.usecases;

import com.questpets.controller.TaskCompletionController;
import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.Task;
import com.questpets.entities.TaskCompletionRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    private static List<Task> tasks = new ArrayList<>();
    private static List<Task> active = new ArrayList<>();
    private static List<TaskCompletionRecord> complete = new ArrayList<>();

//    public void setTaskList() {
//        this.tasks = taskRepository();
//    }
//
//    public void setCompleteTasks() {
//        complete = taskCompletionRepository();
//    }

//    public ResponseEntity<?> findTasks(){
//        List<Task> tas = taskRepo.findAll();
//        return new ResponseEntity<List<Task>>(tas, HttpStatus.OK);
//    }

    public static boolean postCompletedTask(AccountID account, Timestamp timestamp, String task, String image){
        try {
            TaskCompletionController.completeRepo.save(new TaskCompletionRecord(
                    account,
                    timestamp,
                    task,
                    image
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateActiveTasks() {
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
        String td = new Timestamp(System.currentTimeMillis()).toString();
        int today = Integer.parseInt(td.substring(8,10));
        String recent = complete.get(1).getTimestamp().toString().substring(8,10);
        if (Integer.parseInt(recent) != today){
            updateActiveTasks();
        }
        return active;
    }

    public static List<TaskCompletionRecord> getCompleteTasks(SessionID sessionID) {
        return complete;
    }
}