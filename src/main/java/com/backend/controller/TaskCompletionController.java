package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.TaskSystemFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * controller for completed tasks
 */
@RestController
public class TaskCompletionController {
    private final TaskSystemFacade taskSystemFacade;

    /**
     * Spring Boot dependency injection
     * @param taskSystemFacade of type TaskSystemFacade, establishes connection to the task system facade
     */
    public TaskCompletionController(TaskSystemFacade taskSystemFacade){
        this.taskSystemFacade = taskSystemFacade;
    }

    /**
     * Post request to save a TaskCompletionRecord to the database
     * @param sessionID of type String, sessionID references an associated account
     * @param task of type task, the name of the task
     * @param image of type String, the link to the image
     * @param reward of type double, the amount of the task reward
     * @return a response entity detailing successful completion or an associated error
     */
    @PostMapping("/completeTask")
    public ResponseEntity<?> postCompletedTask(@RequestParam String sessionID, String task, String image, double reward){
        return this.taskSystemFacade.completeTask(new SessionID(sessionID), task, image, reward);
    }
}