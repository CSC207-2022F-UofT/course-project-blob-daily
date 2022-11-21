package backend.usecases;

import backend.controller.TaskActiveController;
import backend.controller.TaskCompletionController;
import backend.controller.TaskController;
import backend.entities.IDs.AccountID;
import backend.entities.IDs.SessionID;
import backend.entities.Task;
import backend.entities.TaskActive;
import backend.entities.TaskCompletionRecord;

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
    public static void getActiveTaskList(){
        active = TaskActiveController.activeRepo.findAll();
    }


    public static boolean postCompletedTask(AccountID account, String timestamp, String task, String image){
        try {
            TaskCompletionController.completeRepo.save(new TaskCompletionRecord(
                    account, timestamp, task, image
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateActiveTasks() {
        List<Task> tasks = getTaskList();
        active.clear();
        TaskActiveController.activeRepo.deleteAll();

        Random rand = new Random();
        List<Integer> prev = new ArrayList<>();

        while (active.size() < 3) {
            int num = rand.nextInt(tasks.size()) + 1;
            if (!prev.contains(num)) {
                Task t = tasks.get(num);
                TaskActive tas = new TaskActive(
                        t.getName(), t.getReward(), new Timestamp(System.currentTimeMillis()).toString()
                );
                active.add(tas);
                prev.add(num);
            }
        }
    }

    public static List<TaskActive> getActiveTasks() {
        String td = new Timestamp(System.currentTimeMillis()).toString();
        int today = Integer.parseInt(td.substring(8,10));
        getActiveTaskList();
        String recent = active.get(0).getTimestamp().substring(8,10);
        if (Integer.parseInt(recent) != today) {
            updateActiveTasks();

            for (int x = 0; x < 3; x++){
                TaskActive task = active.get(x);
                TaskActiveController.activeRepo.save(new TaskActive(
                   task.getName(), task.getReward(), task.getTimestamp()
                ));
            }
        }
        return active;
    }

    public static List<TaskCompletionRecord> getCompleteTasks(SessionID sessionID) {
        return complete;
    }
}