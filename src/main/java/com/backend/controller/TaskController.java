package com.backend.controller;

import com.backend.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * controller for tasks
 */
@RestController
public class TaskController {
    //database connection
    public static TaskRepo taskRepo;
    @Autowired
    public TaskController(TaskRepo taskRepo){
        TaskController.taskRepo = taskRepo;
    }
}