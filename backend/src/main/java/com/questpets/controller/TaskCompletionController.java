package com.questpets.controller;

import com.questpets.entities.Task;
import com.questpets.entities.TaskCompletionRecord;
import com.questpets.repositories.TaskCompletionRepo;
import com.questpets.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class TaskCompletionController {
    @PostMapping("/home")
    public ResponseEntity<?> postCompletedTask(@RequestBody String taskName){
        if (TaskManager.postCompletedTask(
                new com.questpets.entities.IDs.AccountID("298aA4#111111111111j"),
                new Timestamp(System.currentTimeMillis()),
                taskName,
                "image"
                //task.getTaskName(),
                //image
        )) {
            return new ResponseEntity<TaskCompletionRecord>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
