package com.backend.usecases;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Task;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.TaskActiveRepo;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Task related use cases
 */
@Service
@Configurable
public class TaskManager {
    /**
     * database connection
     */
    public static TaskRepo taskRepo;
    public static TaskActiveRepo activeRepo;
    public static TaskCompletionRepo completeRepo;

    @SuppressWarnings("unused")
    public TaskManager(TaskRepo taskRepo, TaskActiveRepo activeRepo, TaskCompletionRepo completeRepo){
        TaskManager.taskRepo = taskRepo;
        TaskManager.activeRepo = activeRepo;
        TaskManager.completeRepo = completeRepo;
    }

    /**
     * Post request to create a TaskCompletionRecord in the database
     * @param sessionID of type SessionID, sessionID references an associated account
     * @param task of type String, the name of the task
     * @param image of type String, the URL link of the image
     * @param reward of type double, the reward given when completing a specific task
     * @return a response entity detailing successful completion or an associated error
     */
    public static ResponseEntity<?> postCompletedTask(SessionID sessionID, String task, String image, double reward){
        //verify the sessionID
        AccountID account = AccountManager.verifySession(sessionID);
        if (account == null) {
            return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        //check if the task is part of active tasks
        List <TaskActive> active = TaskManager.activeRepo.findAll();
        for (TaskActive select : active) {
            if (!select.getName().equals(task)) {
                return LogHandler.logError(new Exception("Task does not exist"), HttpStatus.BAD_REQUEST);
            }
        }

        //get a list of completed tasks by accountID
        List <TaskCompletionRecord> complete = getRecord(account);
        String today = new Timestamp(System.currentTimeMillis()).toString().substring(0,10);

        //check if the task has been completed today
        for (TaskCompletionRecord current : complete) {
            String date = current.getTimestamp().substring(0, 10);
            if (date.equals(today) && current.getTask().equals(task)) {
                //if so, return a bad request
                return LogHandler.logError(new Exception("Record already exists"), HttpStatus.BAD_REQUEST);
            }
        }

        //create a new task completion record to be saved and save it to the database
        TaskCompletionRecord completeTask = new TaskCompletionRecord(
                account.getID(), new Timestamp(System.currentTimeMillis()).toString(), task, image);
        TaskManager.completeRepo.save(completeTask);

        //update the balance with the reward
        ShopManager.addBalance(sessionID.toString(), reward);
        return new ResponseEntity<>(completeTask, HttpStatus.OK);
    }

    /**
     * Delete all correlated TaskCompletionRecords when a user deletes their account
     * @param accountID of type AccountID, accountID references associated account
     * @return a response entity detailing successful completion or an associated error
     */
    public static ResponseEntity<?> deleteAllCorrelatedCompletions(AccountID accountID) {
        //get all the records completed by account and delete them
        List<TaskCompletionRecord> complete = getRecord(accountID);
        for (TaskCompletionRecord current : complete) {
            TaskManager.completeRepo.deleteById(current.getID());
        }
        return new ResponseEntity<>("Successfully deleted all correlated records", HttpStatus.OK);
    }

    /**
     * Gets all active tasks that have not yet been completed by sessionID
     * @param sessionID of type SessionID, sessionID references an associated account
     * @return a response entity, if successful a list is returned, else an associated error
     */
    public static ResponseEntity<?> getActiveTasks(SessionID sessionID) {
        //verify the sessionID
        AccountID account = AccountManager.verifySession(sessionID);
        if (account == null) {
            return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        //check the current day and the day the active tasks were updated
        List<TaskActive> active = TaskManager.activeRepo.findAll();
        String today = new Timestamp(System.currentTimeMillis()).toString().substring(0,10);
        String recent = active.get(0).getTimestamp().substring(0,10);

        if (!recent.equals(today)) {
            //if the day has changed, set new active tasks
            newActiveTasks();
        } else {
            //otherwise, update the active tasks removing those that have been completed today
            updateActiveTasks(account, active, today);
        }
        return new ResponseEntity<>(active, HttpStatus.OK);
    }

    /** helper method
     * Gets all completed tasks completed by accountID
     * @param account of type AccountID, references the account
     * @return a list of all tasks completed by accountID
     */
    public static List<TaskCompletionRecord> getRecord(AccountID account) {
        return TaskManager.completeRepo.findAllByAccountID(account.getID());
    }

    /** getActiveTasks() helper method
     * Creates a new set of active tasks and saves it to the database
     */
    public static void newActiveTasks() {
        //empty active tasks in order to replace them
        List<Task> tasks = TaskManager.taskRepo.findAll();
        TaskManager.activeRepo.deleteAll();

        //making sure there's no overlap between tasks
        Random rand = new Random();
        List<Integer> prev = new ArrayList<>();

        //choosing random tasks, changing them into active tasks, and adding them to the array
        while (prev.size() < 3) {
            int num = rand.nextInt(tasks.size());
            if (!prev.contains(num)) {
                Task task = tasks.get(num);

                //create active task from task
                TaskActive activeTask = new TaskActive(task.getName(), task.getReward(),
                        new Timestamp(System.currentTimeMillis()).toString());
                prev.add(num);

                //add the task to the database
                TaskManager.activeRepo.save(activeTask);
            }
        }
    }

    /** getActiveTasks() helper method
     * Updates the active tasks to reflect the tasks that have not yet been completed today
     * @param account of type AccountID, references the account
     * @param active of type List of TaskActive, active references the current active tasks
     * @param today of type String, today references the date today in form yyyy-mm-dd
     */
    public static void updateActiveTasks(AccountID account, List<TaskActive> active, String today) {
        //find completed tasks in order to remove them from active tasks
        List<String> names = new ArrayList<>();
        List<TaskCompletionRecord> complete = getRecord(account);

        //look through the completed tasks to see which task names have been completed today
        for (TaskCompletionRecord current : complete) {
            String date = current.getTimestamp().substring(0, 10);
            if (date.equals(today)) {
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
}