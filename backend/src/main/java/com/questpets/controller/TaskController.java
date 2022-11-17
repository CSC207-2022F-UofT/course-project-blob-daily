package com.questpets.controller;

import com.questpets.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    public static TaskRepo taskRepo;
    @Autowired
    public TaskController(TaskRepo taskRepo){
        TaskController.taskRepo = taskRepo;
    }
}
