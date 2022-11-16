package com.questpets.controller;

import com.questpets.entities.Task;
import com.questpets.repositories.TaskCompletionRepo;
import com.questpets.repositories.TaskRepo;
import com.questpets.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    public static TaskRepo taskRepo;
    @Autowired
    public TaskController(TaskRepo taskRepo){
        TaskController.taskRepo = taskRepo;
    }
    @GetMapping("/")
    public List<Task> getTasks() {
        return taskRepo.findAll();
    }
}
