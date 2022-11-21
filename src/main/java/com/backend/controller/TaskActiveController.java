package com.backend.controller;

import com.backend.entities.TaskActive;
import com.backend.repositories.TaskActiveRepo;
import com.backend.usecases.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskActiveController {
    public static TaskActiveRepo activeRepo;

    @Autowired
    public TaskActiveController(TaskActiveRepo activeRepo){
        TaskActiveController.activeRepo = activeRepo;
    }

    @GetMapping("/activetasks")
    public List<TaskActive> getActiveTasks(@RequestParam String sessionID) {
        return TaskManager.getActiveTasks(sessionID);
    }
}
