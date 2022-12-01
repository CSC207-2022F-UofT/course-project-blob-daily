package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.Task;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;
import com.backend.repositories.TaskActiveRepo;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Task related managers
 */
@Service
@Configurable
public class TaskManager {
    private final TaskRepo taskRepo;
    private final TaskActiveRepo activeRepo;
    private final TaskCompletionRepo completeRepo;

    /**
     * database connection, Spring Boot dependency injection
     * @param taskRepo of type TaskRepo, establishes connection to the task repository
     * @param activeRepo of type TaskActiveRepo, establishes connection to the task active repository
     * @param completeRepo of type TaskCompletionRepo, establishes connection to the task completion repository
     */
    @Autowired
    public TaskManager(TaskRepo taskRepo, TaskActiveRepo activeRepo, TaskCompletionRepo completeRepo){
        this.taskRepo = taskRepo;
        this.activeRepo = activeRepo;
        this.completeRepo = completeRepo;
    }

    /**
     * Post request to create a TaskCompletionRecord in the database
     * @param account of type AccountID, references the account
     * @param name of type String, the name of the task
     * @param image of type String, the URL link of the image
     */
    public TaskCompletionRecord completeTask(AccountID account, String name, String image){
        return this.completeRepo.save(new TaskCompletionRecord(
                account.getID(), new Timestamp(System.currentTimeMillis()).toString(), name, image));
    }

    /**
     * Verifies whether the task name and task reward are valid
     * @param name of type String, the name of the task
     * @param reward of type double, the reward from the task
     * @return true if the task name and reward are valid, false otherwise
     */
    public boolean verifyTask(String name, double reward) {
        //check if the task is part of active tasks and reward is correct
        List <TaskActive> active = this.activeRepo.findAll();
        TaskActive current = null;
        for (TaskActive task : active) {
            if (task.getName().equals(name)){
                current = task;
            }
        }

        if (current == null)  {
            return false;
        }
        return current.getReward() == reward;
    }

    /**
     * Delete all correlated TaskCompletionRecords when a user deletes their account
     * @param accountID of type AccountID, accountID references associated account
     */
    public void deleteAllCorrelatedCompletions(AccountID accountID) {
        //get all the records completed by account and delete them
        List<TaskCompletionRecord> complete = getTaskCompletionRecords(accountID);
        for (TaskCompletionRecord task : complete) {
            this.completeRepo.deleteById(task.getID());
        }
    }

    /**
     * Gets all completed tasks completed by accountID
     * @param account of type AccountID, references the account
     * @return a list of all tasks completed by accountID
     */
    public List<TaskCompletionRecord> getTaskCompletionRecords(AccountID account) {
        return this.completeRepo.findAllByAccountID(account.getID());
    }

    /**
     * Checks if the task has already been completed today
     * @param name of type String, the name of the task
     * @param account of AccountID, references the account
     * @return true is the task has already been completed, false otherwise
     */
    public boolean checkCompleted(String name, AccountID account) {
        List<TaskCompletionRecord> taskCompletionRecords = getTaskCompletionRecords(account);
        String today = new Timestamp(System.currentTimeMillis()).toString().substring(0,10);

        //check if the task has been completed today
        for (TaskCompletionRecord current : taskCompletionRecords) {
            String date = current.getTimestamp().substring(0, 10);
            if (date.equals(today) && current.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a list of active tasks for the user
     * @param account of type AccountID, references the account
     * @return a list of active tasks for the account
     */
    public List<TaskActive> activeTasks(AccountID account){
        List<TaskActive> taskActives = this.activeRepo.findAll();
        String today = new Timestamp(System.currentTimeMillis()).toString().substring(0,10);
        String recent = taskActives.get(0).getTimestamp().substring(0,10);

        if (recent.equals(today)){
            return updateActiveTasks(account, taskActives);
        }
        else {
            return newActiveTasks();
        }
    }

    /** activeTasks() helper method
     * Creates a new set of active tasks and saves it to the database
     */
    public List<TaskActive> newActiveTasks() {
        //empty active tasks in order to replace them
        List<Task> tasks = this.taskRepo.findAll();
        System.out.println(tasks.get(0).getName());
        List<Integer> prev = new ArrayList<>();
        Random rand = new Random();

        this.activeRepo.deleteAll();

        //choosing random tasks, changing them into active tasks, and adding them to the array
        while (prev.size() < 3) {
            int num = rand.nextInt(tasks.size());
            if (!prev.contains(num)) {
                Task task = tasks.get(num);
                prev.add(num);

                System.out.println(task.getName());

                this.activeRepo.save(new TaskActive(task.getName(), task.getReward()    ,
                        new Timestamp(System.currentTimeMillis()).toString()));
            }
        }
        return this.activeRepo.findAll();
    }

    /** activeTasks() helper method
     * Updates the active tasks to reflect the tasks that have not yet been completed today
     * @param account of type AccountID, references the account
     * @param taskActives of type List of TaskActive, references the current active tasks
     */
    public List<TaskActive> updateActiveTasks(AccountID account, List<TaskActive> taskActives) {
        //find completed tasks in order to remove them from active tasks
        String today = new Timestamp(System.currentTimeMillis()).toString().substring(0,10);
        List<String> names = new ArrayList<>();
        List<TaskCompletionRecord> complete = getTaskCompletionRecords(account);

        //look through the completed tasks to see which task names have been completed today
        for (TaskCompletionRecord current : complete) {
            String date = current.getTimestamp().substring(0, 10);
            if (date.equals(today)) {
                names.add(current.getName());
            }
        }

        //remove the tasks with the same names as those found in today's completed tasks
        for (String name : names) {
            for (int x = 0; x < taskActives.size(); x++) {
                if (name.equals(taskActives.get(x).getName())) {
                    taskActives.remove(x);
                    break;
                }
            }
        }
        return taskActives;
    }
}