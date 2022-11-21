package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.repositories.TaskActiveRepo;
import com.backend.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * controller for active tasks
 */
@RestController
public class TaskActiveController {
    /**
     * Get request to get the current uncompleted active tasks based on sessionID
     * @param sessionID of type String, sessionID references an associated account
     * @return a response entity detailing successful completion or an associated error
     */
    @GetMapping("/activeTasks")
    public ResponseEntity<?> getActiveTasks(@RequestParam String sessionID) {
        return TaskManager.getActiveTasks(new SessionID(sessionID));
    }
}
