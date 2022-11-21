package com.backend.controller;

import com.backend.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    public static TaskRepo taskRepo;
    @Autowired
    public TaskController(TaskRepo taskRepo){
        TaskController.taskRepo = taskRepo;
    }
}
