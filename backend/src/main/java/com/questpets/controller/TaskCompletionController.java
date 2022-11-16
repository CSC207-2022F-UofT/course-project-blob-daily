package com.questpets.controller;

import com.questpets.entities.IDs.AccountID;
import com.questpets.entities.TaskCompletionRecord;
import com.questpets.repositories.TaskCompletionRepo;
import com.questpets.usecases.TaskManager;
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
    @PostMapping("/home")
    public ResponseEntity<?> postCompletedTask(@RequestBody String task){
        AccountID a = new AccountID(null);
        a.generateID();
        if (TaskManager.postCompletedTask(
                a,
                new Timestamp(System.currentTimeMillis()),
                "task",
                "image"
                )) {
            return new ResponseEntity<TaskCompletionRecord>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
