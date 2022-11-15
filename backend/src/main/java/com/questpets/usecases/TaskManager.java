package com.questpets.usecases;

import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.IDs.SessionID;
import com.questpets.entities.Task;
import com.questpets.entities.TaskCompletionRecord;
import com.questpets.repositories.TaskCompletionRepo;
import com.questpets.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Configurable
public class TaskManager {
    private static List<Task> tasks = new ArrayList<>();
    private static List<Task> active = new ArrayList<>();
    private static List<TaskCompletionRecord> complete = new ArrayList<>();
    @Autowired
    private static TaskCompletionRepo completeRepo;
    @Autowired
    private static TaskRepo taskRepo;

//    public void setTaskList() {
//        this.tasks = taskRepository();
//    }
//
//    public void setCompleteTasks() {
//        complete = taskCompletionRepository();
//    }

    public ResponseEntity<?> findTasks(){
        List<Task> tas = taskRepo.findAll();
        return new ResponseEntity<List<Task>>(tas, HttpStatus.OK);
    }

    @PostMapping("/home")
    public static boolean postCompletedTask(AccountID account, Timestamp timestamp, String task, String image){
        try {
            TaskCompletionRecord record = new TaskCompletionRecord(account, timestamp, task, image);
            System.out.println("created record");
            System.out.println(record);
            completeRepo.save(record);
            System.out.println("saved record");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println(account + " " + timestamp + " " + task + " " + image);
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