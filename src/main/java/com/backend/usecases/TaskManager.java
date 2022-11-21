package com.backend.usecases;

import com.backend.controller.TaskActiveController;
import com.backend.controller.TaskCompletionController;
import com.backend.controller.TaskController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Task;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskManager {
    public static List<Task> getTaskList() {
        return TaskController.taskRepo.findAll();
    }

    private static AccountID verifySession(String sessionID) {
        return new AccountID("asdasd");
    }

    /**
     *
     * @param sessionID
     * @param task
     * @param image
     */
    public static void postCompletedTask(String sessionID, String task, String image){
        TaskCompletionController.completeRepo.save(new TaskCompletionRecord(
                verifySession(sessionID), new Timestamp(System.currentTimeMillis()).toString(), task, image
        ));
    }

    //create an array of the new active tasks
    public static void newActiveTasks() {
        //empty active tasks in order to replace them
        List<Task> tasks = getTaskList();
        TaskActiveController.activeRepo.deleteAll();

        //making sure there's no overlap between tasks
        Random rand = new Random();
        List<Integer> prev = new ArrayList<>();

        //choosing random tasks, changing them into active tasks, and adding them to the array
        while (prev.size() < 3) {
            int num = rand.nextInt(tasks.size()) + 1;
            if (!prev.contains(num)) {
                Task t = tasks.get(num);

                //create active task from task
                TaskActive tas = new TaskActive(t.getName(), t.getReward(), new Timestamp(System.currentTimeMillis()).toString());
                prev.add(num);

                //add the task to the database
                TaskActiveController.activeRepo.save(tas);
            }
        }
    }

    //update active tasks to remove the completed ones for today
    public static void updateActiveTasks(String sessionID, List<TaskActive> active, int today) {
        //find completed tasks in order to remove them from active tasks
        AccountID account = AccountManager.verifySession(new SessionID(sessionID));

        List<String> names = new ArrayList<>();
        List<TaskCompletionRecord> complete = TaskCompletionController.completeRepo.findAllByAccountID(account.getID());

        //look through the taskcompletionrecords to see which task names have been completed today
        for (TaskCompletionRecord current : complete) {
            int date = Integer.parseInt(current.getTimestamp().substring(8, 10));
            if (date == today) {
                names.add(current.getTask());
            }
        }

        //remove the tasks with the same names as those found in today's completed tasks
        for (String name : names) {
            for (int y = 0; y < active.size(); y++) {
                if (name.equals(active.get(y).getName())) {
                    active.remove(y);
                    break;
                }
            }
        }
    }

    //get active tasks based on accountID and only return the tasks not yet completed
    public static List<TaskActive> getActiveTasks(String sessionID) {
        //check the current day and the day the active tasks were updated
        List<TaskActive> active = TaskActiveController.activeRepo.findAll();
        int today = Integer.parseInt(new Timestamp(System.currentTimeMillis()).toString().substring(8,10));
        int recent = Integer.parseInt(active.get(0).getTimestamp().substring(8,10));

        if (recent != today) {
            //if the day has changed, set new active tasks
            newActiveTasks();
        } else {
            //otherwise, update the active tasks removing the completed ones
            updateActiveTasks(sessionID, active, today);
        }
        return active;
    }
}