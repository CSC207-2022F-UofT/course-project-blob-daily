package com.questpets.controller;

import com.questpets.entities.Task;
import com.questpets.usecases.TaskManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @GetMapping("/home")
    public ResponseEntity<?> getTasks() {
        return new ResponseEntity<List<Task>>(TaskManager.getActiveTasks(), HttpStatus.OK);
    }
}
