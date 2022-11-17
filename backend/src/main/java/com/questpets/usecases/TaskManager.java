package com.questpets.usecases;

import com.questpets.controller.TaskCompletionController;
import com.questpets.controller.TaskController;
import com.questpets.entities.TaskActive;
import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.Task;
import com.questpets.entities.TaskCompletionRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    private static List<TaskActive> active = new ArrayList<>();
    private static List<TaskCompletionRecord> complete = new ArrayList<>();

    public static List<Task> getTaskList() {
        return TaskController.taskRepo.findAll();
    }
    public static List<TaskCompletionRecord> getCompleteTaskList() {
        return TaskCompletionController.completeRepo.findAll();
    }
    public static List<TaskActive> getActiveTaskList(){
        return TaskController.activeRepo.findAll();
    }


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
        List<Task> tasks = getTaskList();
        active.clear();
        Random rand = new Random();
        List<Integer> prev = new ArrayList<>();

        while (active.size() <= 3) {
            int num = rand.nextInt(tasks.size()) + 1;
            if (!prev.contains(num)) {
                Task t = tasks.get(num);
                TaskActive tas = new TaskActive(t.getName(), t.getReward(), new Timestamp(System.currentTimeMillis()));
                active.add(tas);
                prev.add(num);
            }
        }
    }

    public static List<TaskActive> getActiveTasks() {
        String td = new Timestamp(System.currentTimeMillis()).toString();
        int today = Integer.parseInt(td.substring(8,10));
        String recent = active.get(0).getTimestamp().toString().substring(8,10);
        if (Integer.parseInt(recent) != today){
            updateActiveTasks();
        }
        return active;
    }

    public static List<TaskCompletionRecord> getCompleteTasks(SessionID sessionID) {
        return complete;
    }
}