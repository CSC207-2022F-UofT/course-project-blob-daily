package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.TaskCompletionController;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskActive;
import com.backend.repositories.TaskActiveRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.TaskManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskCompletionControllerTest {
    private SessionID sessionID;
    private String task;
    private final String image = "https://www.saycampuslife.com/wp-content/uploads/2017/10/collegelectures-800x500_c.jpg";
    private double reward;

    private final TaskManager taskManager;
    private final TaskActiveRepo taskActiveRepo;
    private final TaskCompletionController taskCompletionController;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;

    @Autowired
    public TaskCompletionControllerTest(TaskManager taskManager, TaskActiveRepo taskActiveRepo, TaskCompletionController taskCompletionController, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.taskManager = taskManager;
        this.taskActiveRepo = taskActiveRepo;
        this.taskCompletionController = taskCompletionController;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        TaskActive active = taskActiveRepo.findAll().get(0);

        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        task = active.getName();
        reward = active.getReward();
    }

    @AfterEach
    public void tearDown() {
        taskManager.deleteAllCorrelatedCompletions(this.accountManager.verifySession(sessionID));
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void postCompletedTaskTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.OK;

        //action
        ResponseEntity<?> actualResponse = this.taskCompletionController.postCompletedTask(sessionID.toString(), task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), completeTaskMessage);
    }

    @Test
    public void postCompletedTaskInvalidSessionTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        //action
        ResponseEntity<?> actualResponse = this.taskCompletionController.postCompletedTask("bad id", task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), completeTaskMessage);
    }

    @Test
    public void postCompletedTaskAlreadyCompleteTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        //action
        this.taskCompletionController.postCompletedTask(sessionID.toString(), task, image, reward);
        ResponseEntity<?> actualResponse = this.taskCompletionController.postCompletedTask(sessionID.toString(), task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task as it is already completed";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), completeTaskMessage);
    }
}