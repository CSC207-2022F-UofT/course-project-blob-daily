package com.questpets.controller;

import com.questpets.entities.Task;
import com.questpets.entities.TaskActive;
import com.questpets.repositories.TaskActiveRepo;
import com.questpets.repositories.TaskRepo;
import com.questpets.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    public static TaskRepo taskRepo;
    public static TaskActiveRepo activeRepo;
    @Autowired
    public TaskController(TaskRepo taskRepo){
        TaskController.taskRepo = taskRepo;
    }
    @Autowired
    public TaskController(TaskActiveRepo activeRepo){
        TaskController.activeRepo = activeRepo;
    }

    @GetMapping("/")
    public List<Task> getTasks() {
        return TaskManager.getTaskList();
    }
    @GetMapping("/")
    public List<TaskActive> getActiveTasks() {
        return TaskManager.getActiveTaskList();
    }
}
