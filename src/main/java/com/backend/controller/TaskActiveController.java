package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.TaskSystemFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * controller for active tasks
 */
@RestController
public class TaskActiveController {
    private final TaskSystemFacade taskSystemFacade;

    /**
     * Spring Boot dependency injection
     * @param taskSystemFacade of type TaskSystemFacade, establishes connection to the task system facade
     */
    @SuppressWarnings("unused")
    public TaskActiveController(TaskSystemFacade taskSystemFacade){
        this.taskSystemFacade = taskSystemFacade;
    }

    /**
     * Get request to get the current uncompleted active tasks based on sessionID
     * @param sessionID of type String, sessionID references an associated account
     * @return a response entity detailing successful completion or an associated error
     */
    @GetMapping("/activeTasks")
    public ResponseEntity<?> getActiveTasks(@RequestParam String sessionID) {
        return this.taskSystemFacade.getActiveTasks(new SessionID(sessionID));
    }
}