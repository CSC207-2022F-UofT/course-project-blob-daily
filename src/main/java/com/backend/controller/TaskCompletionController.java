package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controller for completed tasks
 */
@RestController
public class TaskCompletionController {
    //database connection
    public static TaskCompletionRepo completeRepo;
    @Autowired
    public TaskCompletionController(TaskCompletionRepo completeRepo){
        TaskCompletionController.completeRepo = completeRepo;
    }

    /**
     * Post request to save a TaskCompletionRecord to the database
     * @param sessionID of type String, sessionID references an associated account
     * @param task of type task, the name of the task
     * @param image of type String, the link to the image
     * @param reward of type double, the amount of the task reward
     * @return a response entity detailing successful completion or an associated error
     */
    @PostMapping("/completeTasks")
    public ResponseEntity<?> postCompletedTask(@RequestParam String sessionID, String task, String image, double reward){
        return TaskManager.postCompletedTask(new SessionID(sessionID), task, image, reward);
    }
}
