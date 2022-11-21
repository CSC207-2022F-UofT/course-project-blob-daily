package com.backend.controller;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class TaskCompletionController {
    public static TaskCompletionRepo completeRepo;
    @Autowired
    public TaskCompletionController(TaskCompletionRepo completeRepo){
        TaskCompletionController.completeRepo = completeRepo;
    }
    @PostMapping("/completetask")
    public ResponseEntity<?> postCompletedTask(@RequestParam String task, String sessionID, String image){
        TaskManager.postCompletedTask(sessionID, task, image);
        return new ResponseEntity<TaskCompletionRecord>(HttpStatus.OK);
    }
}
