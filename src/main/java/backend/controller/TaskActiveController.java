package backend.controller;

import backend.entities.TaskActive;
import backend.repositories.TaskActiveRepo;
import backend.usecases.TaskManager;
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
    public List<TaskActive> getActiveTasks() {
        return TaskManager.getActiveTasks();
    }
}
